package com.aycom.feedback_app.dto.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InviteMemberToOrganizationRequest {
    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Member ID is required")
    private Long memberId;
}
