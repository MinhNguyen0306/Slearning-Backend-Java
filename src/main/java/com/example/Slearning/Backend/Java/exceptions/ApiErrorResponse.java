package com.example.Slearning.Backend.Java.exceptions;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private Integer errorCode;
    private String error;
    private String errorMessage;
}
