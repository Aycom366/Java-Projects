package com.aycom.feedback_app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aycom.feedback_app.dto.member.OrganizationMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationRequest;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.dto.organization.InviteMemberToOrganizationRequest;
import com.aycom.feedback_app.dto.organization.InviteMemberToOrganizationResponse;
import com.aycom.feedback_app.security.MemberPrincipal;
import com.aycom.feedback_app.services.OrganizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/organization")
@RequiredArgsConstructor
@RestController
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<CreateOrganizationResponse> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.createOrganization(request));
    }

    @PostMapping("/invite")
    public ResponseEntity<InviteMemberToOrganizationResponse> inviteMemberToOrganization(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @Valid @RequestBody InviteMemberToOrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organizationService.inviteMemberToOrganization(memberPrincipal.getOrganizationId(),
                        request.getMemberId(),
                        memberPrincipal.getId()));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<OrganizationMemberResponse>> getMembersByOrganizationId(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getMembersByOrganizationId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateOrganizationResponse> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @GetMapping
    public ResponseEntity<List<CreateOrganizationResponse>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }
}
