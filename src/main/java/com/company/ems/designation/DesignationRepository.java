package com.company.ems.designation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, UUID> {

	@Query("SELECT COUNT(d) > 0 FROM Designation d WHERE d.name = :name AND d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
	boolean existsByNameAndCompanyIdAndIsDeletedFalse(@Param("name") String name, @Param("companyId") UUID companyId);

	@Query("SELECT COUNT(d) > 0 FROM Designation d WHERE d.code = :code AND d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
	boolean existsByCodeAndCompanyIdAndIsDeletedFalse(@Param("code") String code, @Param("companyId") UUID companyId);

	@Query("SELECT d FROM Designation d WHERE d.id = :id AND d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
	Optional<Designation> findByIdActiveAndTenantScope(@Param("id") UUID id, @Param("companyId") UUID companyId);

	@Query("SELECT d FROM Designation d WHERE d.isDeleted = false AND " +
		       "(d.company.id = :companyId OR d.company IS NULL) AND " + 
		       "(:search = '' OR :search IS NULL OR " +
		       "LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
		       "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
		Page<Designation> searchActiveDesignationsByTenant(@Param("search") String search,  @Param("companyId") UUID companyId, Pageable pageable);
}