package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.Notice;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.utils.enums.DeviceType;
import com.google.firebase.messaging.BatchResponse;

import java.util.UUID;

public interface NotificationService {

    ApiResponse storeDeviceToken(UUID userId, String token, DeviceType deviceType);

    String sendNotificationToAll(Notice notice, String link);

    ApiResponse subscribeTopic(UUID userId, String topicName);

    ApiResponse unSubscribeTopic(UUID userId, String topicName);

}
