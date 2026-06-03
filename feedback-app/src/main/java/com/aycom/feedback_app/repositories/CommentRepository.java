package com.aycom.feedback_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aycom.feedback_app.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c JOIN c.author WHERE c.feedbackBoardItem.id = :feedbackItemId ORDER BY c.createdAt DESC",
            countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.feedbackBoardItem.id = :feedbackItemId")
    Page<Comment> findByFeedbackBoardItemIdWithAuthor(@Param("feedbackItemId") Long feedbackItemId, Pageable pageable);

}
