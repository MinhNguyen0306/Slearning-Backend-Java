package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.entities.DeviceToken;
import com.example.Slearning.Backend.Java.domain.entities.Notice;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.DeviceTokenRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.NotificationService;
import com.example.Slearning.Backend.Java.utils.enums.DeviceType;
import com.example.Slearning.Backend.Java.utils.enums.TokenType;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    private final UserRepository userRepository;

    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    public ApiResponse storeDeviceToken(UUID userId, String token, DeviceType deviceType) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        Optional<DeviceToken> checkTokenExisted = user.getDeviceTokens().stream()
                .filter(deviceToken -> deviceToken.getDeviceType().equals(deviceType))
                .findFirst();

        DeviceToken deviceToken;
        if(checkTokenExisted.isPresent()) {
            user.dismissDeviceToken(checkTokenExisted.get());
            deviceTokenRepository.delete(checkTokenExisted.get());
            User updatedUser = this.userRepository.save(user);

            deviceToken = DeviceToken.builder()
                    .deviceType(deviceType)
                    .tokenValue(token)
                    .createAt(LocalDateTime.now())
                    .user(updatedUser)
                    .build();
        } else {
            deviceToken = DeviceToken.builder()
                    .deviceType(deviceType)
                    .tokenValue(token)
                    .createAt(LocalDateTime.now())
                    .user(user)
                    .build();
        }

        DeviceToken created = this.deviceTokenRepository.save(deviceToken);
        if(created != null) {
            return ApiResponse.builder()
                    .message("Đã bật thông báo trên thiết bị này")
                    .status("201")
                    .build();
        } else {
            return ApiResponse.builder()
                    .message("Lưu device token thâ bại")
                    .status("500")
                    .build();
        }
    }

    @Override
    public String sendNotificationToAll(Notice notice, String link) {
        List<String> deviceTokens = notice.getDeviceTokens();
//        Notification notification = Notification.builder()
//                .setTitle(notice.getTitle())
//                .setBody(notice.getContent())
//                .setImage(notice.getImageUrl())
//                .build();

        WebpushNotification webpushNotification = WebpushNotification.builder()
                .setTitle(notice.getTitle())
                .setBody(notice.getContent())
                .setImage(notice.getImageUrl())
                .build();

        WebpushFcmOptions webpushFcmOptions = WebpushFcmOptions.withLink(link);

        WebpushConfig webpushConfig = WebpushConfig.builder()
                .setNotification(webpushNotification)
                .setFcmOptions(webpushFcmOptions)
                .putHeader("Authorization:key=", "AAAABy0V8sg:APA91bGSnGavbBNkQkJ7_TF-WmY0JIp7qUS6cSbJzCsEGOh319cRJ-KXDZaT3w7M60nIT7QyZGu9ebDdNMejernnRR4dk1nkaQAKFs34ahM9n0Bp2PZINEwkDHPv7nM1btkXhBUqrdKb")
                .build();

//        MulticastMessage multicastMessage = MulticastMessage.builder()
//                .addAllTokens(deviceTokens)
//                .setWebpushConfig(webpushConfig)
//                .putAllData(notice.getData())
//                .build();
        Message message = Message.builder()
                .setWebpushConfig(webpushConfig)
                .setToken(deviceTokens.get(0))
                .build();

        try {
            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
//        try {
//            BatchResponse batchResponse = firebaseMessaging.sendMulticast(multicastMessage);
//            if (batchResponse.getFailureCount() > 0) {
//                List<SendResponse> responses = batchResponse.getResponses();
//                List<String> failedTokens = new ArrayList<>();
//                for (int i = 0; i < responses.size(); i++) {
//                    if (!responses.get(i).isSuccessful()) {
//                        failedTokens.add(deviceTokens.get(i));
//                    }
//                }
//
//                log.info("List of tokens that caused failures: " + failedTokens);
//            }
//
//            return batchResponse;
//        } catch (FirebaseMessagingException e) {
//            log.error("Firebase error {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public ApiResponse subscribeTopic(UUID userId, String topicName) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        List<String> deviceTokensOfUser = user.getTokens().stream()
                .filter(token -> token.getType().equals(TokenType.DEVICE_TOKEN))
                .map(token -> token.getValue())
                .collect(Collectors.toList());
        if(deviceTokensOfUser.isEmpty()) {
            return ApiResponse.builder()
                    .message("Nguoi dung chua bat thong bao!")
                    .status("400")
                    .build();
        } else {
            try {
                TopicManagementResponse response = firebaseMessaging.subscribeToTopic(deviceTokensOfUser, topicName);
                log.info(String.format("%s tokens were subscribed successfully", response.getSuccessCount()));
                if(response.getFailureCount() > 0) {
                    return ApiResponse.builder()
                            .message("Subscribe topic that bai!")
                            .status("500")
                            .build();
                } else {
                    return ApiResponse.builder()
                            .message("Subscribe topic thanh cong!")
                            .status("201")
                            .build();
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ApiResponse unSubscribeTopic(UUID userId, String topicName) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));
        List<String> deviceTokensOfUser = user.getTokens().stream()
                .filter(token -> token.getType().equals(TokenType.DEVICE_TOKEN))
                .map(token -> token.getValue())
                .collect(Collectors.toList());
        if(deviceTokensOfUser.isEmpty()) {
            return null;
        } else {
            try {
                TopicManagementResponse response = firebaseMessaging.subscribeToTopic(deviceTokensOfUser, topicName);
                log.info(String.format("%s tokens were unSubscribed successfully", response.getSuccessCount()));
                if(response.getFailureCount() > 0) {
                    return ApiResponse.builder()
                            .message("unSubscribe topic that bai!")
                            .status("500")
                            .build();
                } else {
                    return ApiResponse.builder()
                            .message("unSubscribe topic thanh cong!")
                            .status("201")
                            .build();
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
