package com.aycom.feedback_app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.feedback_app.dto.auth.CreateMemberRequestDto;
import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.dto.auth.LoginRequest;
import com.aycom.feedback_app.dto.auth.LoginResponse;
import com.aycom.feedback_app.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CreateMemberResponse> createMember(@Valid @RequestBody CreateMemberRequestDto member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createMember(member));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
