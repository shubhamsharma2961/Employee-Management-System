package com.company.ems.experience;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.company.ems.common.ApprovalStatus;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, UUID>{
	@Query("SELECT e FROM Experience e WHERE e.id = :id AND e.isDeleted = false")
    Optional<Experience> findActiveById(@Param("id") UUID id);

    @Query("SELECT e FROM Experience e WHERE e.employee.id = :employeeId AND e.isDeleted = false ORDER BY e.startDate DESC")
    List<Experience> findAllByEmployeeIdActive(@Param("employeeId") UUID employeeId);

    @Query("SELECT e FROM Experience e WHERE e.status = :status AND e.isDeleted = false ORDER BY e.createdAt ASC")
    List<Experience> findAllByStatusActive(@Param("status") ApprovalStatus status);
}
