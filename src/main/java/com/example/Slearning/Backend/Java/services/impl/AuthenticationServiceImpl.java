package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.LoginDto;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterDto;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;
import com.example.Slearning.Backend.Java.repositories.RoleRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterDto registerDto, Integer type) {
        return null;
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto) {
        return null;
    }
}
