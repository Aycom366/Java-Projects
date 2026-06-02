package com.aycom.feedback_app.dto.member;

import java.time.LocalDateTime;

import com.aycom.feedback_app.models.Member;

import lombok.Builder;

@Builder
public record CreateMemberResponse(

                Long id,
                String name,
                String email,
                String username,
                LocalDateTime joinedAt

) {

        public static CreateMemberResponse toDto(Member member) {
                return CreateMemberResponse.builder()
                                .id(member.getId())
                                .name(member.getName())
                                .email(member.getEmail())
                                .username(member.getUsername())
                                .joinedAt(member.getJoinedAt())
                                .build();
        }
}
