package com.aycom.feedback_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.UpVote;

public interface UpvoteRepository extends JpaRepository<UpVote, Long> {
    public UpVote findByFeedbackBoardItemIdAndMemberId(Long feedbackBoardItemId, Long memberId);
    public int countByFeedbackBoardItemId(Long feedbackBoardItemId);
}
