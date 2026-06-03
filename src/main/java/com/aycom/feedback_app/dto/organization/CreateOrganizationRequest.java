package com.aycom.feedback_app.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {
    @NotBlank(message = "Organization name is required")
    @NotNull(message = "Organization name is required")
    private String name;

    @NotNull(message = "Member ID is required")
    private Integer memberId;

}
