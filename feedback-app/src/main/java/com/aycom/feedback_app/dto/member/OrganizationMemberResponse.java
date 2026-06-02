package com.aycom.feedback_app.dto.member;

import java.time.LocalDateTime;

import com.aycom.feedback_app.enums.RoleEnum;
import com.aycom.feedback_app.models.MemberOrganization;

import lombok.Builder;

@Builder
public record OrganizationMemberResponse(
        Long id,
        String name,
        String email,
        String username,
        LocalDateTime joinedAt,
        RoleEnum role
) {
    public static OrganizationMemberResponse toOrganizationMember(MemberOrganization mo) {
        return OrganizationMemberResponse.builder()
                .id(mo.getMember().getId())
                .name(mo.getMember().getName())
                .email(mo.getMember().getEmail())
                .username(mo.getMember().getUsername())
                .joinedAt(mo.getMember().getJoinedAt())
                .role(mo.getRole())
                .build();
    }
}
