package com.aycom.feedback_app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardRequest;
import com.aycom.feedback_app.models.FeedbackBoard;
import com.aycom.feedback_app.security.MemberPrincipal;
import com.aycom.feedback_app.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackBoard> createFeedbackBoard(
            @Valid @RequestBody CreateFeedbackBoardRequest feedback,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.createFeedbackBoard(feedback, memberPrincipal.getId()));
    }

}
