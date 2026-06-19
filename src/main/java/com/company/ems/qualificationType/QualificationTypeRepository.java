package com.company.ems.qualificationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QualificationTypeRepository extends JpaRepository<QualificationType, UUID> {

	@Query("SELECT q FROM QualificationType q WHERE q.isDeleted = false AND (q.company.id = :companyId OR q.company.id IS NULL)")
    List<QualificationType> findAllActiveAndTenantScope(@Param("companyId") UUID companyId);

    @Query("SELECT q FROM QualificationType q WHERE q.id = :id AND q.isDeleted = false AND (q.company.id = :companyId OR q.company.id IS NULL)")
    Optional<QualificationType> findByIdActiveAndTenantScope(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query("SELECT q FROM QualificationType q WHERE q.name = :name AND q.isDeleted = false AND (q.company.id = :companyId OR q.company.id IS NULL)")
    Optional<QualificationType> findByNameActiveAndTenantScope(@Param("name") String name, @Param("companyId") UUID companyId);

    @Query("SELECT COUNT(q) > 0 FROM QualificationType q WHERE q.name = :name AND q.isDeleted = false AND (q.company.id = :companyId OR q.company.id IS NULL)")
    boolean existsByNameActiveAndTenantScope(@Param("name") String name, @Param("companyId") UUID companyId);

    @Query("SELECT q FROM QualificationType q WHERE q.name = :name AND q.company.id IS NULL AND q.isDeleted = false")
    Optional<QualificationType> findGlobalByNameActive(@Param("name") String name);
}