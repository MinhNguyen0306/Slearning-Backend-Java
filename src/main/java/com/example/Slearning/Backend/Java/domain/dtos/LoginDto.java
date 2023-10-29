package com.example.Slearning.Backend.Java.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    private String username;
    private String password;
}
