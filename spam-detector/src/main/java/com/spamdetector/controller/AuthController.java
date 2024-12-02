package com.spamdetector.controller;

import com.spamdetector.dto.*;
import com.spamdetector.security.JwtTokenUtil;
import com.spamdetector.service.AuthenticationService;
import com.spamdetector.dto.UserProfileDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = authenticationService.login(loginRequest.getPhoneNumber(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public UserProfileDTO register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        return authenticationService.registerUser(userRegistrationDTO);
    }
}