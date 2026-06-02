package com.aycom.feedback_app.dto.organization;

import java.time.LocalDateTime;

import com.aycom.feedback_app.enums.RoleEnum;
import com.aycom.feedback_app.models.MemberOrganization;

import lombok.Builder;

@Builder
public record InviteMemberToOrganizationResponse(
        Long organizationId,
        Long memberId,
        String memberName,
        String memberEmail,
        String memberUsername,
        LocalDateTime joinedAt,
        RoleEnum role) {
    public static InviteMemberToOrganizationResponse toInviteMemberToOrganization(
            MemberOrganization memberOrganization) {
        return InviteMemberToOrganizationResponse.builder()
                .organizationId(memberOrganization.getOrganization().getId())
                .memberId(memberOrganization.getMember().getId())
                .memberName(memberOrganization.getMember().getName())
                .memberEmail(memberOrganization.getMember().getEmail())
                .memberUsername(memberOrganization.getMember().getUsername())
                .joinedAt(memberOrganization.getMember().getJoinedAt())
                .role(memberOrganization.getRole())
                .build();
    }
}