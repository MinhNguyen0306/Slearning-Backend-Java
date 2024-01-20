package com.example.Slearning.Backend.Java.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private Integer errorCode;
    private String error;
    private String errorMessage;
}
