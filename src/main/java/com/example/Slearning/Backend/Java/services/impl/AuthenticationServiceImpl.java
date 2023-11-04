package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.LoginRequest;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterRequest;
import com.example.Slearning.Backend.Java.domain.entities.Role;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.mappers.UserMapper;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;
import com.example.Slearning.Backend.Java.exceptions.ApiException;
import com.example.Slearning.Backend.Java.exceptions.DuplicateException;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.RoleRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.AuthenticationService;
import com.example.Slearning.Backend.Java.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request, Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "Id", roleId));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        List<User> users = userRepository.findAll();
        boolean checkEmailExisted = users.stream().anyMatch(user -> user.getEmail() == request.getEmail());
        if(checkEmailExisted) {
            throw new DuplicateException("User", "Email", request.getEmail());
        }

        User user = null;

        switch (role.getRole()) {
            case "instructor":
                user = User.builder()
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .roles(roles)
                        .isInstructor(true)
                        .build();
                break;
            default:
                user = User.builder()
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .roles(roles)
                        .build();
                break;
        }

        User savedUser = userRepository.save(user);

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return AuthenticationResponse.builder()
                .userDto(userMapper.userToDto(user))
                .tokens(tokens)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            var user = userRepository.findByEmail(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Email", request.getUsername()));

            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            return AuthenticationResponse.builder()
                    .userDto(userMapper.userToDto(user))
                    .tokens(tokens)
                    .build();
        } catch (BadCredentialsException exception) {
            log.error("Bad Credentials", exception.getMessage());
            throw new ApiException("Invalid Username or password");
        }
    }
}
