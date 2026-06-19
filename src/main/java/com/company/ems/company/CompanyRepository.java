package com.company.ems.company;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
	@Query("SELECT c FROM Company c WHERE c.id = :id AND c.isDeleted = false")
	Optional<Company> findActiveById(@Param("id") UUID id);
}