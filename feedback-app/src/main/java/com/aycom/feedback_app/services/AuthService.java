package com.aycom.feedback_app.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.aycom.feedback_app.dto.auth.LoginRequest;
import com.aycom.feedback_app.dto.auth.LoginResponse;
import com.aycom.feedback_app.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        return LoginResponse.builder()
                .token(jwtUtil.generateToken(request.email()))
                .email(request.email())
                .build();

    }
}
