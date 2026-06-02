package com.aycom.feedback_app.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.aycom.feedback_app.security.MemberPrincipal;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;

import com.aycom.feedback_app.dto.member.CreateMemberRequestDto;
import com.aycom.feedback_app.dto.member.CreateMemberResponse;
import com.aycom.feedback_app.dto.organization.CreateOrganizationResponse;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.repositories.MemberOrganizationRepository;
import com.aycom.feedback_app.repositories.MemberRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberOrganizationRepository memberOrganizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public CreateMemberResponse createMember(CreateMemberRequestDto member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new DataIntegrityViolationException(
                    "Member with email " + member.getEmail() + " already exists. Please login instead.");
        }

        String hash = passwordEncoder.encode(member.getPassword());
        String base = member.getName().toLowerCase().replaceAll("\\s+", "");

        String username = memberRepository.existsByUsername(base) ? base +
                random.nextInt(999) : base;

        Member newMember = Member.builder()
                .name(member.getName())
                .email(member.getEmail())
                .username(username)
                .passwordHash(hash)
                .build();

        memberRepository.save(newMember);

        return CreateMemberResponse.toDto(newMember);

    }

    public List<CreateOrganizationResponse> getOrganizationsByMemberId(Long memberId) {
        return memberOrganizationRepository.findByMemberIdWithOrganization(memberId).stream()
                .map(mo -> mo.getOrganization()).map(CreateOrganizationResponse::toOrganization)
                .collect(Collectors.toList());
    }

    public List<CreateMemberResponse> getAllMembers() {

        return memberRepository.findAll().stream()
                .map(CreateMemberResponse::toDto)
                .collect(Collectors.toList());
    }

    public List<CreateMemberResponse> searchMembersByEmail(String email) {
        return memberRepository.findAllByEmailStartingWithIgnoreCase(email).stream()
                .map(CreateMemberResponse::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new MemberPrincipal(member);
    }

}
