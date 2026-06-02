package com.aycom.feedback_app.dto.feedback;

import com.aycom.feedback_app.models.FeedBackBoardItem;

public record FeedbackItemWithCounts(FeedBackBoardItem item, Long upVotesCount, Long commentsCount) {
}
