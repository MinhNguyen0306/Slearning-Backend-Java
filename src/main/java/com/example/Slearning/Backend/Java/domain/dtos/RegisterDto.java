package com.example.Slearning.Backend.Java.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {
    private String fullName;
    private String email;
    private String password;
}
