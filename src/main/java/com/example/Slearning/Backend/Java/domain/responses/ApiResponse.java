package com.example.Slearning.Backend.Java.domain.responses;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class ApiResponse {
    private String message;
    private String status;
}
