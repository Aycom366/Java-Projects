package com.aycom.feedback_app.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aycom.feedback_app.models.Organization;
import com.aycom.feedback_app.repositories.MemberOrganizationRepository;
import com.aycom.feedback_app.repositories.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final MemberOrganizationRepository memberOrganizationRepository;
    private final OrganizationRepository organizationRepository;

    public Organization requireOrganizationMember(Long memberId, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));

        if (!memberOrganizationRepository.existsByMemberIdAndOrganizationId(memberId, organizationId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this organization");
        }

        return organization;
    }
}
