package com.aycom.feedback_app.dto.feedback;

import com.aycom.feedback_app.enums.CategoryEnum;
import com.aycom.feedback_app.enums.FeedbackStateEnum;

public record UpdateFeedbackBoardItemRequest(
        String title,
        String details,
        CategoryEnum category,
        FeedbackStateEnum state) {
}