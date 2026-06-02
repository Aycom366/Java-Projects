package com.aycom.feedback_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByEmailStartingWithIgnoreCase(String email);

}
