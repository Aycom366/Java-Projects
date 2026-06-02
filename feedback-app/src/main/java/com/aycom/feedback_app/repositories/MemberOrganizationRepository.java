package com.aycom.feedback_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aycom.feedback_app.enums.RoleEnum;
import com.aycom.feedback_app.models.Member;
import com.aycom.feedback_app.models.MemberOrganization;
import com.aycom.feedback_app.models.Organization;

public interface MemberOrganizationRepository extends JpaRepository<MemberOrganization, Long> {
    boolean existsByMemberAndRole(Member member, RoleEnum role);

    boolean existsByMemberAndOrganization(Member member, Organization organization);

    boolean existsByMemberIdAndOrganizationId(Long memberId, Long organizationId);

    @Query("SELECT mo FROM MemberOrganization mo JOIN FETCH mo.organization WHERE mo.member.id = :memberId")
    List<MemberOrganization> findByMemberIdWithOrganization(Long memberId);

}
