package com.aycom.feedback_app.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aycom.feedback_app.dto.feedback.CommentResponse;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardItemRequest;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardRequest;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackBoardResponse;
import com.aycom.feedback_app.dto.feedback.CreateFeedbackItemResponse;
import com.aycom.feedback_app.dto.feedback.SubCommentResponse;
import com.aycom.feedback_app.dto.feedback.UpdateFeedbackBoardItemRequest;
import com.aycom.feedback_app.models.Comment;
import com.aycom.feedback_app.models.FeedBackBoardItem;
import com.aycom.feedback_app.models.FeedbackBoard;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.models.Organization;
import com.aycom.feedback_app.models.SubComment;
import com.aycom.feedback_app.models.UpVote;
import com.aycom.feedback_app.repositories.CommentRepository;
import com.aycom.feedback_app.repositories.FeedbackItemRepository;
import com.aycom.feedback_app.repositories.FeedbackRepository;
import com.aycom.feedback_app.repositories.MemberRepository;
import com.aycom.feedback_app.repositories.SubCommentRepository;
import com.aycom.feedback_app.repositories.UpvoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {
        private final FeedbackItemRepository feedbackItemRepository;
        private final FeedbackRepository feedbackRepository;
        private final AuthorizationService authorizationService;
        private final MemberRepository memberRepository;
        private final UpvoteRepository upvoteRepository;
        private final CommentRepository commentRepository;
        private final SubCommentRepository subCommentRepository;

        private record FeedbackItemWithOrg(FeedBackBoardItem item, Organization organization) {
        }

        public CreateFeedbackBoardResponse createFeedbackBoard(CreateFeedbackBoardRequest request, Long memberId,
                        Long organizationId) {
                Organization organization = authorizationService.requireOrganizationMember(memberId, organizationId);

                FeedbackBoard feedbackBoard = new FeedbackBoard();
                feedbackBoard.setTitle(request.title());
                feedbackBoard.setDescription(request.description());
                feedbackBoard.setOrganization(organization);
                return CreateFeedbackBoardResponse.toDto(feedbackRepository.save(feedbackBoard));
        }

        public List<CreateFeedbackBoardResponse> getFeedbackBoardsByOrganizationId(Long memberId, Long organizationId) {
                authorizationService.requireOrganizationMember(memberId, organizationId);

                return feedbackRepository.findByOrganizationId(organizationId).stream()
                                .map(CreateFeedbackBoardResponse::toDto)
                                .collect(Collectors.toList());

        }

        @Transactional
        public CreateFeedbackItemResponse createFeedbackBoardItem(CreateFeedbackBoardItemRequest request, Long memberId,
                        Long organizationId) {
                authorizationService.requireOrganizationMember(memberId, organizationId);

                FeedbackBoard feedbackBoard = feedbackRepository.findById(request.feedbackBoardId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Feedback board not found"));

                Member member = memberRepository.getReferenceById(memberId);

                FeedBackBoardItem feedBackBoardItem = FeedBackBoardItem.builder()
                                .title(request.title()).details(request.details()).category(request.category())
                                .feedbackBoard(feedbackBoard).createdBy(member).build();

                feedbackItemRepository.save(feedBackBoardItem);

                return CreateFeedbackItemResponse.toDto(feedBackBoardItem);
        }

        public CreateFeedbackItemResponse updateFeedbackBoardItem(
                        UpdateFeedbackBoardItemRequest request, Long memberId,
                        Long organizationId, Long feedbackItemId) {
                FeedbackItemWithOrg feedbackItemWithOrg = requireFeedbackBoardItem(feedbackItemId, memberId,
                                organizationId);

                feedbackItemWithOrg.item()
                                .setTitle(request.title() != null ? request.title()
                                                : feedbackItemWithOrg.item().getTitle());
                feedbackItemWithOrg.item()
                                .setDetails(request.details() != null ? request.details()
                                                : feedbackItemWithOrg.item().getDetails());
                feedbackItemWithOrg.item().setCategory(
                                request.category() != null ? request.category()
                                                : feedbackItemWithOrg.item().getCategory());
                feedbackItemWithOrg.item()
                                .setState(request.state() != null ? request.state()
                                                : feedbackItemWithOrg.item().getState());
                feedbackItemRepository.save(feedbackItemWithOrg.item());

                return CreateFeedbackItemResponse.toDto(feedbackItemWithOrg.item());
        }

        public Page<CreateFeedbackItemResponse> getAllFeedbackItems(Long memberId, Long organizationId,
                        Pageable pageable) {
                authorizationService.requireOrganizationMember(memberId, organizationId);

                return feedbackItemRepository.findWithCountsByOrganizationId(organizationId, pageable)
                                .map(CreateFeedbackItemResponse::toDto);
        }

        public void deleteFeedbackBoardItem(Long feedbackItemId, Long memberId, Long organizationId) {
                FeedbackItemWithOrg feedbackItemWithOrg = requireFeedbackBoardItem(feedbackItemId, memberId,
                                organizationId);
                feedbackItemRepository.delete(feedbackItemWithOrg.item());
        }

        @Transactional
        public CreateFeedbackItemResponse upvoteFeedbackItem(Long feedbackItemId, Long memberId, Long organizationId) {

                FeedbackItemWithOrg feedbackItemWithOrg = requireFeedbackBoardItem(feedbackItemId, memberId,
                                organizationId);

                UpVote existingUpVote = upvoteRepository.findByFeedbackBoardItemIdAndMemberId(feedbackItemId, memberId);

                if (existingUpVote != null) {
                        upvoteRepository.delete(existingUpVote);
                } else {
                        Member member = memberRepository.getReferenceById(memberId);
                        UpVote upVote = new UpVote();
                        upVote.setFeedbackBoardItem(feedbackItemWithOrg.item());
                        upVote.setMember(member);
                        upvoteRepository.save(upVote);
                }

                int upvotesCount = upvoteRepository.countByFeedbackBoardItemId(feedbackItemId);
                return CreateFeedbackItemResponse.toDto(feedbackItemWithOrg.item(), upvotesCount);

        }

        public CommentResponse createComment(Long feedbackItemId, Long memberId, Long organizationId, String body) {
                FeedbackItemWithOrg feedbackItemWithOrg = requireFeedbackBoardItem(feedbackItemId, memberId,
                                organizationId);
                Member member = memberRepository.getReferenceById(memberId);
                Comment comment = Comment.builder().body(body).feedbackBoardItem(feedbackItemWithOrg.item())
                                .author(member)
                                .build();
                return CommentResponse.toDto(commentRepository.save(comment));
        }

        private FeedbackItemWithOrg requireFeedbackBoardItem(Long feedbackItemId, Long memberId, Long organizationId) {
                FeedBackBoardItem feedBackBoardItem = feedbackItemRepository.findById(feedbackItemId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Feedback item not found"));
                Organization organization = authorizationService.requireOrganizationMember(memberId, organizationId);
                return new FeedbackItemWithOrg(feedBackBoardItem, organization);
        }

        public Page<CommentResponse> getCommentsByFeedbackItemId(Long feedbackItemId, Long memberId,
                        Long organizationId, Pageable pageable) {
                authorizationService.requireOrganizationMember(memberId, organizationId);
                return commentRepository.findByFeedbackBoardItemIdWithAuthor(feedbackItemId, pageable)
                                .map(CommentResponse::toDto);
        }

        public SubCommentResponse createSubComment(Long feedbackItemId, Long commentId, Long memberId,
                        Long organizationId,
                        String body, Long parentSubCommentId) {
                requireFeedbackBoardItem(feedbackItemId, memberId, organizationId);
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Comment not found"));
                Member member = memberRepository.getReferenceById(memberId);
                SubComment subComment = new SubComment();
                subComment.setBody(body);
                subComment.setComment(comment);
                subComment.setAuthor(member);
                subComment.setParentSubComment(parentSubCommentId != null
                                ? subCommentRepository.findById(parentSubCommentId)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                "Parent subcomment not found"))
                                : null);
                return SubCommentResponse.toDto(subCommentRepository.save(subComment));

        }

        public Page<SubCommentResponse> getSubCommentsByCommentId(Long feedbackItemId, Long commentId, Long memberId,
                        Long organizationId, Pageable pageable) {
                requireFeedbackBoardItem(feedbackItemId, memberId, organizationId);
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Comment not found"));

                writeJsonFile(comment, "comment.json");

                return subCommentRepository.findByCommentIdOrderByCreatedAtDesc(commentId, pageable)
                                .map(SubCommentResponse::toDto);
        }

        private void writeJsonFile(Object object, String fileName) {
                try {
                        new ObjectMapper()
                                        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                                        .writerWithDefaultPrettyPrinter()
                                        .writeValue(new File(fileName), object);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to write JSON file: " + fileName, e);
                }
        }

}
