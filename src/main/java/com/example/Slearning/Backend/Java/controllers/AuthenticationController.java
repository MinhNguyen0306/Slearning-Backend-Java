package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.dtos.LoginRequest;
import com.example.Slearning.Backend.Java.domain.dtos.RegisterRequest;
import com.example.Slearning.Backend.Java.domain.responses.AuthenticationResponse;
import com.example.Slearning.Backend.Java.exceptions.ApiException;
import com.example.Slearning.Backend.Java.exceptions.DuplicateException;
import com.example.Slearning.Backend.Java.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
        @RequestBody @Valid LoginRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationService.login(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ApiException exception) {
            throw exception;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody @Valid RegisterRequest request,
        @RequestParam(value = "role", defaultValue = "2", required = false) Integer roleId
    ) {
        try {
            AuthenticationResponse response = authenticationService.register(request, roleId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DuplicateException exception) {
            throw exception;
        }
    }
}
