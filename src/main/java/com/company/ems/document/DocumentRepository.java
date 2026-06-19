package com.company.ems.document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

	@Query("SELECT d FROM Document d WHERE d.id = :id AND d.company.id = :companyId AND d.isDeleted = false")
    Optional<Document> findActiveByIdAndCompanyId(@Param("id") UUID id, @Param("companyId") UUID companyId);

    // 🔒 Fetch all active documents belonging strictly to the logged-in company
    @Query("SELECT d FROM Document d WHERE d.company.id = :companyId AND d.isDeleted = false")
    List<Document> findAllActiveDocumentsByCompanyId(@Param("companyId") UUID companyId);

    // 🔒 Fetch active documents by type, strictly scoped to the logged-in company
    @Query("SELECT d FROM Document d WHERE d.documentType.id = :typeId AND d.company.id = :companyId AND d.isDeleted = false")
    List<Document> findActiveByDocumentTypeIdAndCompanyId(@Param("typeId") UUID typeId, @Param("companyId") UUID companyId);
    
    @Query("SELECT d FROM Document d WHERE d.id IN :ids AND d.company.id = :companyId AND d.isDeleted = false")
    List<Document> findAllByIdAndCompanyId(@Param("ids") List<UUID> ids, @Param("companyId") UUID companyId);
}