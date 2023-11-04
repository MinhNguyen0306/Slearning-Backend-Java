package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.LoginRequest;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterRequest;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;

import java.util.UUID;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerDto, Integer roleId);
    AuthenticationResponse login(LoginRequest loginDto);
}
