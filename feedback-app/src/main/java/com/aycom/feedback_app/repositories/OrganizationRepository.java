package com.aycom.feedback_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aycom.feedback_app.models.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
