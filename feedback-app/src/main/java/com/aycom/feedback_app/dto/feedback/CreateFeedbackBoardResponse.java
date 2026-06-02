package com.aycom.feedback_app.dto.feedback;

import java.time.LocalDateTime;

import com.aycom.feedback_app.models.FeedbackBoard;

import lombok.Builder;

@Builder
public record CreateFeedbackBoardResponse(Long id, String title, String description, LocalDateTime createdAt,
        Long organizationId) {

    public static CreateFeedbackBoardResponse toDto(FeedbackBoard feedbackBoard) {
        return CreateFeedbackBoardResponse.builder().id(feedbackBoard.getId()).title(feedbackBoard.getTitle())
                .description(feedbackBoard.getDescription()).createdAt(feedbackBoard.getCreatedAt())
                .organizationId(feedbackBoard.getOrganization().getId()).build();
    }
}
