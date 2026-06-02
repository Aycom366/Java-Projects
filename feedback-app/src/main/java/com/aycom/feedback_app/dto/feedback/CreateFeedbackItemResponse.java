package com.aycom.feedback_app.dto.feedback;

import java.time.LocalDateTime;

import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.enums.CategoryEnum;
import com.aycom.feedback_app.enums.FeedbackStateEnum;
import com.aycom.feedback_app.models.FeedBackBoardItem;

import lombok.Builder;

@Builder
public record CreateFeedbackItemResponse(Long id, String title, String details, CategoryEnum category,
        FeedbackStateEnum state, LocalDateTime createdAt, CreateMemberResponse createdBy,
        int upVotesCount,
        int commentsCount,
        CreateFeedbackBoardResponse feedbackBoard) {

    public static CreateFeedbackItemResponse toDto(FeedBackBoardItem feedBackBoardItem) {
        return CreateFeedbackItemResponse.builder()
                .id(feedBackBoardItem.getId())
                .title(feedBackBoardItem.getTitle())
                .details(feedBackBoardItem.getDetails())
                .category(feedBackBoardItem.getCategory())
                .state(feedBackBoardItem.getState())
                .createdAt(feedBackBoardItem.getCreatedAt())
                .createdBy(CreateMemberResponse.toDto(feedBackBoardItem.getCreatedBy()))
                .feedbackBoard(CreateFeedbackBoardResponse.toDto(feedBackBoardItem.getFeedbackBoard()))
                .upVotesCount(feedBackBoardItem.getUpVotes() != null ? feedBackBoardItem.getUpVotes().size() : 0)
                .commentsCount(feedBackBoardItem.getComments() != null ? feedBackBoardItem.getComments().size() : 0)
                .build();
    }

    public static CreateFeedbackItemResponse toDto(FeedBackBoardItem feedBackBoardItem, int upvotesCount) {
        return CreateFeedbackItemResponse.builder()
                .id(feedBackBoardItem.getId())
                .title(feedBackBoardItem.getTitle())
                .details(feedBackBoardItem.getDetails())
                .category(feedBackBoardItem.getCategory())
                .state(feedBackBoardItem.getState())
                .createdAt(feedBackBoardItem.getCreatedAt())
                .createdBy(CreateMemberResponse.toDto(feedBackBoardItem.getCreatedBy()))
                .feedbackBoard(CreateFeedbackBoardResponse.toDto(feedBackBoardItem.getFeedbackBoard()))
                .upVotesCount(upvotesCount)
                .commentsCount(feedBackBoardItem.getComments().size())
                .build();
    }

    public static CreateFeedbackItemResponse toDto(FeedbackItemWithCounts projection) {
        FeedBackBoardItem f = projection.item();
        return CreateFeedbackItemResponse.builder()
                .id(f.getId())
                .title(f.getTitle())
                .details(f.getDetails())
                .category(f.getCategory())
                .state(f.getState())
                .createdAt(f.getCreatedAt())
                .createdBy(CreateMemberResponse.toDto(f.getCreatedBy()))
                .feedbackBoard(CreateFeedbackBoardResponse.toDto(f.getFeedbackBoard()))
                .upVotesCount(projection.upVotesCount().intValue())
                .commentsCount(projection.commentsCount().intValue())
                .build();
    }
}