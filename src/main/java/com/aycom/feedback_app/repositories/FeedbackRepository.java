package com.aycom.feedback_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.FeedbackBoard;

public interface FeedbackRepository extends JpaRepository<FeedbackBoard, Long> {
    public List<FeedbackBoard> findByOrganizationId(Long organizationId);
}
