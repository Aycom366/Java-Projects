package com.aycom.feedback_app.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardRequest;
import com.aycom.feedback_app.models.FeedbackBoard;
import com.aycom.feedback_app.models.Organization;
import com.aycom.feedback_app.repositories.FeedbackRepository;
import com.aycom.feedback_app.repositories.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthorizationService authorizationService;

    public FeedbackBoard createFeedbackBoard(CreateFeedbackBoardRequest request, Long memberId) {
        Organization organization = authorizationService.requireOrganizationMember(memberId, request.organizationId());

        FeedbackBoard feedbackBoard = new FeedbackBoard();
        feedbackBoard.setTitle(request.title());
        feedbackBoard.setDescription(request.description());
        feedbackBoard.setOrganization(organization);
        return feedbackRepository.save(feedbackBoard);
    }

    public List<FeedbackBoard> getFeedbackBoardsByOrganizationId(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found"));
        return organization.getFeedbackBoards();
    }

}
