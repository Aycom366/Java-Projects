package com.aycom.feedback_app.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.aycom.feedback_app.enums.CategoryEnum;
import com.aycom.feedback_app.enums.FeedbackStateEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBackBoardItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String details;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private FeedbackStateEnum state = FeedbackStateEnum.PLANNED;

    @ManyToOne
    @JsonBackReference("board-items")
    @JoinColumn(name = "boardId")
    private FeedbackBoard feedbackBoard;

    @ManyToOne
    @JsonBackReference("member-feedbackItems")
    @JoinColumn(name = "createdById")
    private Member createdBy;

    @JsonManagedReference("item-upvotes")
    @OneToMany(mappedBy = "feedbackBoardItem", cascade = CascadeType.ALL)
    private List<UpVote> upVotes;

    @OneToMany(mappedBy = "feedbackBoardItem", cascade = CascadeType.ALL)
    @JsonManagedReference("item-comments")
    private List<Comment> comments;

}
