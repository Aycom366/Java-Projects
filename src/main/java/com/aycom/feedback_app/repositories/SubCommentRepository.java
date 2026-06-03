package com.aycom.feedback_app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.SubComment;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    Page<SubComment> findByCommentIdOrderByCreatedAtDesc(Long commentId, Pageable pageable);
}
