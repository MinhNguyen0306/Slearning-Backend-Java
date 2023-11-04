package com.example.Slearning.Backend.Java.domain.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
}
