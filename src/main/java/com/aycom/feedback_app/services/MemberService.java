package com.aycom.feedback_app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.aycom.feedback_app.security.MemberPrincipal;

import org.springframework.stereotype.Service;

import com.aycom.feedback_app.dto.auth.CreateMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.repositories.MemberOrganizationRepository;
import com.aycom.feedback_app.repositories.MemberRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberOrganizationRepository memberOrganizationRepository;

    public List<CreateOrganizationResponse> getOrganizationsByMemberId(Long memberId) {
        return memberOrganizationRepository.findByMemberIdWithOrganization(memberId).stream()
                .map(mo -> mo.getOrganization()).map(CreateOrganizationResponse::toOrganization)
                .collect(Collectors.toList());
    }

    public Page<CreateMemberResponse> getAllMembers(String searchQuery, Pageable pageable) {
        if (searchQuery != null) {
            return memberRepository.searchByEmailOrName(searchQuery, pageable)
                    .map(CreateMemberResponse::toDto);
        }

        return memberRepository.findAll(pageable)
                .map(CreateMemberResponse::toDto);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new MemberPrincipal(member);
    }

}
