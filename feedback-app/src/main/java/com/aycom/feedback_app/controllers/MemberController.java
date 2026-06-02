package com.aycom.feedback_app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.feedback_app.dto.member.CreateMemberRequestDto;
import com.aycom.feedback_app.dto.member.CreateMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.security.MemberPrincipal;
import com.aycom.feedback_app.services.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember(@Valid @RequestBody CreateMemberRequestDto member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(member));
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<CreateOrganizationResponse>> getOrganizationsByMemberId() {
        MemberPrincipal userDetails = (MemberPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(memberService.getOrganizationsByMemberId(userDetails.getId()));

    }

    @GetMapping("/all-members")
    public ResponseEntity<List<CreateMemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CreateMemberResponse>> searchByEmail(@RequestParam String email) {
        return ResponseEntity.ok(memberService.searchMembersByEmail(email));
    }

}
