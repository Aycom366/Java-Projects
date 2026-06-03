package com.aycom.feedback_app.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aycom.feedback_app.models.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Member> searchByEmailOrName(@Param("query") String query, Pageable pageable);

}
