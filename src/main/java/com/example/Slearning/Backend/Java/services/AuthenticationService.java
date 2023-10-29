package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.LoginDto;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterDto;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterDto registerDto, Integer type);
    AuthenticationResponse login(LoginDto loginDto);
}
