package com.aycom.feedback_app.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "feedback_board_item_id",
        "member_id" }), indexes = @Index(name = "idx_upvote_feedback_board_item_id", columnList = "feedback_board_item_id"))
public class UpVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime upvotedAt;

    @ManyToOne
    @JoinColumn(name = "feedbackBoardItemId")
    @JsonBackReference("item-upvotes")
    private FeedBackBoardItem feedbackBoardItem;

    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonBackReference("member-upvotes")
    private Member member;
}
