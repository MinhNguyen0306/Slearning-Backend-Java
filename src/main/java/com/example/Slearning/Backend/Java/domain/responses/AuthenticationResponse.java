package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class AuthenticationResponse {
    UserDto user;
    Map<String, String> tokens;
}
