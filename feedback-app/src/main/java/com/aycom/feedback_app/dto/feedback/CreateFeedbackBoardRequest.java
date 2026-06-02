package com.aycom.feedback_app.dto.feedback;

import jakarta.validation.constraints.NotBlank;

public record CreateFeedbackBoardRequest(
        @NotBlank(message = "Title is required") String title,

        String description

) {

}
