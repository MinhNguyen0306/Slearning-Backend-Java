package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.entities.Notice;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.services.NotificationService;
import com.example.Slearning.Backend.Java.utils.enums.DeviceType;
import com.google.firebase.messaging.BatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/notification/firebase")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/store/device-token")
    public ResponseEntity<ApiResponse> storeDeviceToken(
            @RequestParam("userId") UUID userId,
            @RequestParam("token") String token,
            @RequestParam("deviceType") DeviceType deviceType
    ) {
        ApiResponse response = this.notificationService.storeDeviceToken(userId, token, deviceType);
        if(response.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestBody Notice notice, @RequestParam("link") String link) {
        String batchResponse = this.notificationService.sendNotificationToAll(notice, link);
        return ResponseEntity.ok(batchResponse);
    }

    @PostMapping("/{userId}/subscribe-topic")
    public ResponseEntity<ApiResponse> subscribeTopic(
            @PathVariable UUID userId,
            @RequestParam("topic") String topicName
    ) {
        ApiResponse response = this.notificationService.subscribeTopic(userId, topicName);
        if(response.getStatus() == "400") {
            return ResponseEntity.badRequest().body(response);
        } else if(response.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(response);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PostMapping("/{userId}/unsubscribe-topic")
    public ResponseEntity<ApiResponse> unSubscribeTopic(
            @PathVariable UUID userId,
            @RequestParam("topic") String topicName
    ) {
        ApiResponse response = this.notificationService.unSubscribeTopic(userId, topicName);
        if(response.getStatus() == "400") {
            return ResponseEntity.badRequest().body(response);
        } else if(response.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(response);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
}
