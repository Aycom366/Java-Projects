package com.aycom.feedback_app.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFeedbackBoardRequest(
        @NotBlank(message = "Title is required") String title,

        String description,

        @NotNull(message = "Organization ID is required") Long organizationId) {

}
