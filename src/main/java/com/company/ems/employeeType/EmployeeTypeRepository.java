package com.company.ems.employeeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, UUID> {
	
	@Query("SELECT et FROM EmployeeType et WHERE et.isDeleted = false AND (et.company.id = :companyId OR et.company.id IS NULL)")
    List<EmployeeType> findAllActiveAndTenantScope(@Param("companyId") UUID companyId);

    @Query("SELECT et FROM EmployeeType et WHERE et.id = :id AND et.isDeleted = false AND (et.company.id = :companyId OR et.company.id IS NULL)")
    Optional<EmployeeType> findByIdActiveAndTenantScope(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query("SELECT COUNT(et) > 0 FROM EmployeeType et WHERE et.name = :name AND et.isDeleted = false AND (et.company.id = :companyId OR et.company IS NULL)")
    boolean existsByNameAndCompanyIdAndIsDeletedFalse(String name, UUID companyId);

    @Query("SELECT et FROM EmployeeType et WHERE et.code = :code AND et.isDeleted = false AND (et.company.id = :companyId OR et.company.id IS NULL)")
    Optional<EmployeeType> findByCodeActiveAndTenantScope(@Param("code") String code, @Param("companyId") UUID companyId);
}
