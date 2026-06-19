package com.company.ems.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID>{
	
	Optional<Employee> findByIdAndCompanyId(UUID id, UUID companyId);
	
boolean existsByEmailAndCompanyId(String email, UUID companyId);
    
    Optional<Employee> findByEmailAndCompanyId(String email, UUID companyId);
    
    Optional<Employee> findByUserIdAndCompanyId(UUID userId, UUID companyId);

    // Kept fallback user search for core authentication setups if company context isn't parsed yet
    Optional<Employee> findByUserId(UUID userId);

    // 🔒 2. Tenant-Scoped Department Extraction
    @Query("SELECT e.department.id FROM Employee e WHERE e.id = :employeeId AND e.company.id = :companyId AND e.isDeleted = false")
    UUID findDepartmentIdByEmployeeIdAndCompanyId(@Param("employeeId") UUID employeeId, @Param("companyId") UUID companyId);
    
    // 🔒 3. Tenant-Scoped List Ordering
    List<Employee> findAllByCompanyIdAndIsDeletedFalseOrderByEmployeeCodeAsc(UUID companyId);

    // 🔒 4. Deep Entity Fetching with Anti-BOLA Multi-Tenant Guards
    @EntityGraph(attributePaths = {"qualifications", "experiences"})
    @Query("SELECT e FROM Employee e WHERE e.id = :id AND e.company.id = :companyId AND e.isDeleted = false")
    Optional<Employee> findEmployeeWithDetailsByIdAndCompanyId(@Param("id") UUID id, @Param("companyId") UUID companyId);

    // ⚡ 5. Database Sequence (Global Database Level - Context Safe)
    @Query(value = "SELECT nextval('employee_code_seq')", nativeQuery = true)
    Long getNextEmployeeSequenceValue();

    // 🔒 6. Optimized Tenant-Scoped Search & Pagination (Converted to JPQL for safe multi-dialect execution)
    @Query("SELECT e FROM Employee e WHERE " +
           "(e.company.id = :companyId) AND (e.isDeleted = false) AND " +
           "(:search IS NULL OR :search = '' " +
           "OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(e.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
           "(:designationId IS NULL OR e.designation.id = :designationId) AND " +
           "(:employeeTypeId IS NULL OR e.employeeType.id = :employeeTypeId)")
    Page<Employee> searchEmployeesTenantScoped(
        @Param("search") String search, 
        @Param("departmentId") UUID departmentId, 
        @Param("designationId") UUID designationId, 
        @Param("employeeTypeId") UUID employeeTypeId, 
        @Param("companyId") UUID companyId,
        Pageable pageable
    );

}
