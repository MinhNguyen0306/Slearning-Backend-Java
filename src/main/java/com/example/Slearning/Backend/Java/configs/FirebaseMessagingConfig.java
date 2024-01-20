package com.example.Slearning.Backend.Java.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FirebaseMessagingConfig {
    private final FirebaseProperties firebaseProperties;

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials googleCredentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    GoogleCredentials googleCredentials() throws IOException {
        if (firebaseProperties.getServiceAccount() != null) {
            return GoogleCredentials.fromStream(new ClassPathResource(firebaseProperties.getServiceAccount()).getInputStream());
        } else {
            return GoogleCredentials.getApplicationDefault();
        }
    }
}
