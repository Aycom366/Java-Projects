package com.aycom.feedback_app.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.security.MemberPrincipal;
import com.aycom.feedback_app.services.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/organizations")
    public ResponseEntity<List<CreateOrganizationResponse>> getOrganizationsByMemberId() {
        MemberPrincipal userDetails = (MemberPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(memberService.getOrganizationsByMemberId(userDetails.getId()));

    }

    @GetMapping
    public ResponseEntity<List<CreateMemberResponse>> getAllMembers(
            @RequestParam(required = false) String searchQuery) {
        return ResponseEntity.ok(memberService.getAllMembers(searchQuery));
    }

}
