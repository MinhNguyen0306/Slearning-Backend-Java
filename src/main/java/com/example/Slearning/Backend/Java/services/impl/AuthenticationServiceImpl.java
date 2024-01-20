package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.LoginRequest;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterRequest;
import com.example.Slearning.Backend.Java.domain.entities.Role;
import com.example.Slearning.Backend.Java.domain.entities.Token;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.entities.VerificationToken;
import com.example.Slearning.Backend.Java.domain.mappers.UserMapper;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;
import com.example.Slearning.Backend.Java.exceptions.ApiException;
import com.example.Slearning.Backend.Java.exceptions.DuplicateException;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.RoleRepository;
import com.example.Slearning.Backend.Java.repositories.TokenRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.repositories.VerificationTokenRepository;
import com.example.Slearning.Backend.Java.services.AuthenticationService;
import com.example.Slearning.Backend.Java.services.JWTService;
import com.example.Slearning.Backend.Java.utils.enums.TokenType;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${token.verification.expiration.minute}")
    private long expiredVerificationTokenMinute;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

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
            case "mentor":
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
                        .userStatus(UserStatus.ACTIVE)
                        .build();
                break;
        }

        User savedUser = userRepository.save(user);

//        // TODO: Send verification token
//        String generatedToken = UUID.randomUUID().toString();
//        VerificationToken verificationToken = VerificationToken
//                .builder()
//                .tokenValue(generatedToken)
//                .createAt(LocalDateTime.now())
//                .expiredAt(LocalDateTime.now().plusMinutes(expiredVerificationTokenMinute))
//                .user(savedUser)
//                .build();
//        this.verificationTokenRepository.save(verificationToken);
//
//        // TODO: Send email

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user.getId(), refreshToken);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return AuthenticationResponse.builder()
                .user(userMapper.userToDto(user))
                .tokens(tokens)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Email", request.getEmail()));

            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeUserToken(user.getId());
            saveUserToken(user.getId(), refreshToken);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            return AuthenticationResponse.builder()
                    .user(userMapper.userToDto(user))
                    .tokens(tokens)
                    .build();
        } catch (BadCredentialsException exception) {
            log.error("Bad Credentials", exception.getMessage());
            throw new ApiException("Invalid Username or password");
        }
    }

    @Override
    public ApiResponse verifyToken(String verificationTokenValue) {
        VerificationToken verificationToken = this.verificationTokenRepository.findByToken(verificationTokenValue)
                .orElseThrow(() -> new ResourceNotFoundException("VerificationToken", "tokenValue", verificationTokenValue));
        if(verificationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already verified");
        }

        LocalDateTime expiredAt = verificationToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired!");
        }

        verificationToken.setConfirmedAt(LocalDateTime.now());

        User user = verificationToken.getUser();
        user.setEnabled(true);
        this.userRepository.save(user);
        this.verificationTokenRepository.save(verificationToken);

        return ApiResponse.builder()
                .message("Email has verified")
                .status("verified")
                .build();
    }

    @Override
    public void saveUserToken(UUID userId, String tokenValue) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Token token = Token.builder()
                .user(user)
                .value(tokenValue)
                .type(TokenType.REFRESH_TOKEN)
                .createAt(LocalDateTime.now())
                .build();
        this.tokenRepository.save(token);
    }

    @Override
    public void revokeUserToken(UUID userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Optional<Token> existToken = user.getTokens().stream()
                .filter(token -> token.getType().equals(TokenType.REFRESH_TOKEN)).findFirst();
        if(existToken.isPresent()) {
            Token token = existToken.get();
            user.dismissToken(token);
            this.tokenRepository.delete(token);
        }
    }

    @Override
    public void refreshToken(UUID userId, HttpServletResponse response) throws IOException {
        final String refreshToken;
        final String userEmail;

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Optional<Token> token = user.getTokens().stream()
                .filter(reToken -> reToken.getType().equals(TokenType.REFRESH_TOKEN))
                .findFirst();

        if(!token.isPresent()) {
            return;
        }

        //Lấy ra refresh token từ user
        refreshToken = token.get().getValue();

        //Extract email từ JWT Token
        userEmail = jwtService.extractUsername(refreshToken);

        //Kiểm tra token có còn trong security context không nếu còn thì bỏ qua
        if(userEmail != null) {
            var userDetails = this.userRepository.findByEmail(userEmail).orElseThrow();

            //Kiểm tra token hợp lệ
            if(jwtService.isTokenValid(refreshToken, userDetails)) {
                //Tạo ra một auth token với email và password hiện tại
                var accessToken = this.jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponse
                   .builder()
                   .tokens(
                       Map.ofEntries(
                           Map.entry("access_token", accessToken),
                           Map.entry("refresh_token", refreshToken)
                       )
                   )
                   .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            } else {
                var apiResponse = ApiResponse.builder()
                        .message("Refresh token had expired")
                        .status("404")
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
            }
        }
    }
}
