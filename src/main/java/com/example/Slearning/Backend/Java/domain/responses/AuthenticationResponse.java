package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AuthenticationResponse {
    UserDto userDto;
    Map<String, String> tokens;
}
