package com.aycom.feedback_app.dto.organization;

import java.time.LocalDateTime;

import com.aycom.feedback_app.models.Organization;

import lombok.Builder;

@Builder
public record CreateOrganizationResponse(

        Long id,
        String name,
        LocalDateTime createdAt

) {
    public static CreateOrganizationResponse toOrganization(Organization org) {
        return CreateOrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .createdAt(org.getCreatedAt())
                .build();
    }
}