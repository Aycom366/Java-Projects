package com.aycom.feedback_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.SubComment;
import java.util.List;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    List<SubComment> findByCommentIdOrderByCreatedAtDesc(Long commentId);
}
