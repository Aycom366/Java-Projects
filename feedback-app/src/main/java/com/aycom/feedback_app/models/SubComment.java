package com.aycom.feedback_app.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "idx_subcomment_comment_id", columnList = "comment_id"))
public class SubComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonBackReference("member-subcomments")
    @JoinColumn(name = "authorId")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("comment-subcomments")
    @JoinColumn(name = "commentId")
    private Comment comment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentSubCommentId", nullable = true)
    private SubComment parentSubComment;

}
