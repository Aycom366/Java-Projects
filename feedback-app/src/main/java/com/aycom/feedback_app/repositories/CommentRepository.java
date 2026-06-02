package com.aycom.feedback_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aycom.feedback_app.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.feedbackBoardItem.id = :feedbackItemId ORDER BY c.createdAt DESC")
    List<Comment> findByFeedbackBoardItemIdWithAuthor(@Param("feedbackItemId") Long feedbackItemId);

}
