package com.aycom.feedback_app.dto.feedback;

import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.models.Comment;

public record CommentResponse(Long id, String body, CreateMemberResponse author) {
    public static CommentResponse toDto(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getBody(), CreateMemberResponse.toDto(comment.getAuthor()));
    }
}
