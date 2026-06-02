package com.aycom.feedback_app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aycom.feedback_app.dto.member.OrganizationMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationRequest;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.dto.organization.InviteMemberToOrganizationResponse;
import com.aycom.feedback_app.enums.RoleEnum;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.models.MemberOrganization;
import com.aycom.feedback_app.models.Organization;
import com.aycom.feedback_app.repositories.MemberOrganizationRepository;
import com.aycom.feedback_app.repositories.MemberRepository;
import com.aycom.feedback_app.repositories.OrganizationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {

        private final OrganizationRepository organizationRepository;
        private final MemberOrganizationRepository memberOrganizationRepository;
        private final MemberRepository memberRepository;
        private final AuthorizationService authorizationService;

        public CreateOrganizationResponse getOrganizationById(Long id) {
                Organization organization = organizationRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Organization does not exist"));
                return CreateOrganizationResponse.toOrganization(organization);
        }

        public InviteMemberToOrganizationResponse inviteMemberToOrganization(Long organizationId, Long memberId,
                        Long memberPrincipalId) {
                Organization organization = authorizationService.requireOrganizationMember(memberPrincipalId,
                                organizationId);

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Member does not exist"));

                if (memberOrganizationRepository.existsByMemberIdAndOrganizationId(memberId, organizationId)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "Member is already a member of the organization");
                }

                MemberOrganization memberOrganization = MemberOrganization.builder()
                                .member(member)
                                .organization(organization)
                                .role(RoleEnum.TESTER)
                                .build();
                memberOrganizationRepository.save(memberOrganization);

                return InviteMemberToOrganizationResponse.toInviteMemberToOrganization(memberOrganization);

        }

        public List<OrganizationMemberResponse> getMembersByOrganizationId(Long organizationId,
                        Long memberPrincipalId) {
                Organization organization = authorizationService.requireOrganizationMember(memberPrincipalId,
                                organizationId);
                return organization.getMemberOrganization().stream()
                                .map(OrganizationMemberResponse::toOrganizationMember)
                                .collect(Collectors.toList());
        }

        public List<CreateOrganizationResponse> getAllOrganizations() {
                return organizationRepository.findAll().stream()
                                .map(CreateOrganizationResponse::toOrganization)
                                .collect(Collectors.toList());
        }

        @Transactional
        public CreateOrganizationResponse createOrganization(CreateOrganizationRequest request) {
                Member member = memberRepository.findById(request.getMemberId().longValue())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Member does not exist"));

                if (memberOrganizationRepository.existsByMemberAndRole(member, RoleEnum.OWNER)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "Member is already an owner of an organization");
                }

                Organization organization = Organization.builder()
                                .name(request.getName())
                                .build();

                MemberOrganization memberOrganization = MemberOrganization.builder()
                                .member(member)
                                .organization(organization)
                                .role(RoleEnum.OWNER)
                                .build();

                organization.setMemberOrganization(List.of(memberOrganization));

                organizationRepository.save(organization);

                return CreateOrganizationResponse.toOrganization(organization);
        }
}
