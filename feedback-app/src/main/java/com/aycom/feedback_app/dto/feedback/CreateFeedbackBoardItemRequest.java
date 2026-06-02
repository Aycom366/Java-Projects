package com.aycom.feedback_app.dto.feedback;

import com.aycom.feedback_app.enums.CategoryEnum;
import com.aycom.feedback_app.enums.FeedbackStateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFeedbackBoardItemRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Details are required") String details,
        @NotNull(message = "Category is required") CategoryEnum category,

        @NotNull(message = "Feedback board ID is required") Long feedbackBoardId,

        FeedbackStateEnum state) {

}
