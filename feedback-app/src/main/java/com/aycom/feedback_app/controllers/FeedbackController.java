package com.aycom.feedback_app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.feedback_app.dto.feedback.CommentResponse;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardItemRequest;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardRequest;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardResponse;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackItemResponse;
import com.aycom.feedback_app.dto.feedback.SubCommentRequest;
import com.aycom.feedback_app.dto.feedback.SubCommentResponse;
import com.aycom.feedback_app.dto.feedback.UpdateFeedbackBoardItemRequest;
import com.aycom.feedback_app.models.SubComment;
import com.aycom.feedback_app.security.MemberPrincipal;
import com.aycom.feedback_app.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
        private final FeedbackService feedbackService;

        private record CommentRequest(String body) {
        }

        @PostMapping
        public ResponseEntity<CreateFeedbackBoardResponse> createFeedbackBoard(
                        @Valid @RequestBody CreateFeedbackBoardRequest feedback,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(feedbackService.createFeedbackBoard(feedback, memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId()));
        }

        @GetMapping
        public ResponseEntity<List<CreateFeedbackBoardResponse>> getFeedbackBoards(
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.ok(feedbackService.getFeedbackBoardsByOrganizationId(memberPrincipal.getId(),
                                memberPrincipal.getOrganizationId()));
        }

        @PostMapping("/item/create")
        public ResponseEntity<CreateFeedbackItemResponse> createFeedbackItem(
                        @Valid @RequestBody CreateFeedbackBoardItemRequest request,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(feedbackService.createFeedbackBoardItem(request, memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId()));
        }

        @PutMapping("/item/{feedbackItemId}")
        public ResponseEntity<CreateFeedbackItemResponse> updateFeedbackItem(
                        @PathVariable Long feedbackItemId,
                        @Valid @RequestBody UpdateFeedbackBoardItemRequest request,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.ok()
                                .body(feedbackService.updateFeedbackBoardItem(request, memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId(), feedbackItemId));
        }

        @PostMapping("/item/{feedbackItemId}/upvote")
        public ResponseEntity<CreateFeedbackItemResponse> upvoteFeedbackItem(
                        @PathVariable Long feedbackItemId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.ok()
                                .body(feedbackService.upvoteFeedbackItem(feedbackItemId, memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId()));
        }

        @DeleteMapping("/item/{feedbackItemId}")
        public ResponseEntity<Void> deleteFeedbackItem(
                        @PathVariable Long feedbackItemId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                feedbackService.deleteFeedbackBoardItem(feedbackItemId, memberPrincipal.getId(),
                                memberPrincipal.getOrganizationId());
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/items/all")
        public ResponseEntity<List<CreateFeedbackItemResponse>> getAllFeedbackItems(
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.ok(feedbackService.getAllFeedbackItems(memberPrincipal.getId(),
                                memberPrincipal.getOrganizationId()));
        }

        @PostMapping("/item/{feedbackItemId}/comment")
        public ResponseEntity<CommentResponse> createComment(@PathVariable Long feedbackItemId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal,
                        @Valid @RequestBody CommentRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.createComment(feedbackItemId,
                                memberPrincipal.getId(), memberPrincipal.getOrganizationId(), request.body()));
        }

        @GetMapping("/item/{feedbackItemId}/comments")
        public ResponseEntity<List<CommentResponse>> getCommentsByFeedbackItemId(@PathVariable Long feedbackItemId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity
                                .ok(feedbackService.getCommentsByFeedbackItemId(feedbackItemId, memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId()));
        }

        @PostMapping("/item/{feedbackItemId}/comment/{commentId}/subcomment")
        public ResponseEntity<SubCommentResponse> createSubComment(@PathVariable Long feedbackItemId,
                        @PathVariable Long commentId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal,
                        @Valid @RequestBody SubCommentRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(feedbackService.createSubComment(feedbackItemId, commentId,
                                                memberPrincipal.getId(),
                                                memberPrincipal.getOrganizationId(), request.body(),
                                                request.parentSubCommentId()));
        }

        @GetMapping("/item/{feedbackItemId}/comment/{commentId}/subcomments")
        public ResponseEntity<List<SubCommentResponse>> getSubCommentsByCommentId(@PathVariable Long feedbackItemId,
                        @PathVariable Long commentId,
                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
                return ResponseEntity.ok(feedbackService.getSubCommentsByCommentId(feedbackItemId, commentId,
                                memberPrincipal.getId(), memberPrincipal.getOrganizationId()));
        }

}
