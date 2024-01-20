package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.LoginRequest;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterRequest;
import com.example.Slearning.Backend.Java.domain.entities.VerificationToken;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest registerDto, Integer roleId);

    AuthenticationResponse login(LoginRequest loginDto);

    ApiResponse verifyToken(String verificationTokenValue);

    void saveUserToken(UUID userId, String token);

    void revokeUserToken(UUID userId);

    void refreshToken(UUID userId, HttpServletResponse response) throws IOException;
}
