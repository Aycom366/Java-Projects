package com.aycom.feedback_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aycom.feedback_app.dto.feedback.FeedbackItemWithCounts;
import com.aycom.feedback_app.models.FeedBackBoardItem;

public interface FeedbackItemRepository extends JpaRepository<FeedBackBoardItem, Long> {

    public List<FeedBackBoardItem> findByFeedbackBoardOrganizationId(Long organizationId);

    @Query("SELECT new com.aycom.feedback_app.dto.feedback.FeedbackItemWithCounts(f, COUNT(DISTINCT u), COUNT(DISTINCT c)) " +
            "FROM FeedBackBoardItem f " +
            "LEFT JOIN f.upVotes u " +
            "LEFT JOIN f.comments c " +
            "WHERE f.feedbackBoard.organization.id = :organizationId " +
            "GROUP BY f")
    List<FeedbackItemWithCounts> findWithCountsByOrganizationId(@Param("organizationId") Long organizationId);
}
