package com.aycom.feedback_app.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    @JsonManagedReference("member-memberOrg")
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberOrganization> memberOrganizations;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonManagedReference("member-feedbackItems")
    private List<FeedBackBoardItem> feedbackBoardItems;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference("member-upvotes")
    private List<UpVote> upVotes;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference("member-comments")
    private List<Comment> comments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference("member-subcomments")
    private List<SubComment> subComments;

}
