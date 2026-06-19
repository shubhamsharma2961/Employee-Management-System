package com.company.ems.documentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {

	@Query("SELECT d FROM DocumentType d WHERE d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
    List<DocumentType> findAllActiveAndTenantScope(@Param("companyId") UUID companyId);

    // 🚀 Secures singular lookups within the allowed multi-tenant boundary
    @Query("SELECT d FROM DocumentType d WHERE d.id = :id AND d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
    Optional<DocumentType> findByIdActiveAndTenantScope(@Param("id") UUID id, @Param("companyId") UUID companyId);

    // 🚀 Unique check matching tenant scope or system defaults
    @Query("SELECT d FROM DocumentType d WHERE LOWER(d.name) = LOWER(:name) AND d.isDeleted = false AND (d.company.id = :companyId OR d.company IS NULL)")
    Optional<DocumentType> findByNameActiveAndTenantScope(@Param("name") String name, @Param("companyId") UUID companyId);

    // 🚀 Check used during the Copy-on-Write fork to ensure they don't duplicate names in their own company rows
    @Query("SELECT COUNT(d) > 0 FROM DocumentType d WHERE LOWER(d.name) = LOWER(:name) AND d.isDeleted = false AND d.company.id = :companyId")
    boolean existsByNameAndCompanyIdAndIsDeletedFalse(@Param("name") String name, @Param("companyId") UUID companyId);
    
    @Query("SELECT d FROM DocumentType d WHERE d.isDeleted = false " +
            "AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (d.company.id = :companyId OR d.company IS NULL)")
     List<DocumentType> searchActiveDocumentTypesByTenant(@Param("search") String search, @Param("companyId") UUID companyId);
}
