package com.aycom.feedback_app.dto.feedback;

import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.models.SubComment;

public record SubCommentResponse(Long id, String body, CreateMemberResponse replyTo, CreateMemberResponse author) {
    public static SubCommentResponse toDto(SubComment subComment) {
        return new SubCommentResponse(subComment.getId(), subComment.getBody(),
                subComment.getParentSubComment() != null
                        ? CreateMemberResponse.toDto(subComment.getParentSubComment().getAuthor())
                        : null,
                CreateMemberResponse.toDto(subComment.getAuthor()));
    }
}
