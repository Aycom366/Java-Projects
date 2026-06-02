package com.aycom.feedback_app.services;

import java.util.Random;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aycom.feedback_app.dto.auth.CreateMemberRequestDto;
import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.dto.auth.LoginRequest;
import com.aycom.feedback_app.dto.auth.LoginResponse;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.repositories.MemberRepository;
import com.aycom.feedback_app.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final AuthenticationManager authenticationManager;
        private final JWTUtil jwtUtil;
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final Random random = new Random();

        public CreateMemberResponse createMember(CreateMemberRequestDto member) {
                if (memberRepository.existsByEmail(member.getEmail())) {
                        throw new DataIntegrityViolationException(
                                        "Member with email " + member.getEmail()
                                                        + " already exists. Please login instead.");
                }

                String hash = passwordEncoder.encode(member.getPassword());
                String base = member.getName().toLowerCase().replaceAll("\\s+", "");

                String username = memberRepository.existsByUsername(base) ? base +
                                random.nextInt(999) : base;

                Member newMember = Member.builder()
                                .name(member.getName())
                                .email(member.getEmail())
                                .username(username)
                                .passwordHash(hash)
                                .build();

                memberRepository.save(newMember);

                return CreateMemberResponse.toDto(newMember);

        }

        public LoginResponse login(LoginRequest request) {
                authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                                                request.password()));
                return LoginResponse.builder()
                                .token(jwtUtil.generateToken(request.email()))
                                .email(request.email())
                                .build();

        }
}
