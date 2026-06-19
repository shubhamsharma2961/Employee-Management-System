package com.company.ems.qualification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.company.ems.common.ApprovalStatus;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, UUID> {
	@Query("SELECT q FROM Qualification q WHERE q.id = :id AND q.company.id = :companyId AND q.isDeleted = false")
    Optional<Qualification> findByIdAndCompanyId(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query("SELECT q FROM Qualification q WHERE q.employee.id = :employeeId AND q.company.id = :companyId AND q.isDeleted = false ORDER BY q.yearOfCompletion DESC")
    List<Qualification> findAllByEmployeeIdAndCompanyIdAndIsDeletedFalse(@Param("employeeId") UUID employeeId, @Param("companyId") UUID companyId);

    @Query("SELECT q FROM Qualification q WHERE q.status = :status AND q.company.id = :companyId AND q.isDeleted = false ORDER BY q.createdAt ASC")
    List<Qualification> findAllByStatusAndCompanyIdAndIsDeletedFalse(@Param("status") ApprovalStatus status, @Param("companyId") UUID companyId);

    @Query("""
           SELECT DISTINCT q FROM Qualification q
           LEFT JOIN FETCH q.documents d
           LEFT JOIN FETCH d.documentType
           WHERE q.employee.id = :employeeId
           AND q.company.id = :companyId
           AND q.isDeleted = false
           ORDER BY q.yearOfCompletion DESC
           """)
    List<Qualification> findAllByEmployeeWithDocumentsAndCompanyId(
        @Param("employeeId") UUID employeeId, 
        @Param("companyId") UUID companyId
    );
}
