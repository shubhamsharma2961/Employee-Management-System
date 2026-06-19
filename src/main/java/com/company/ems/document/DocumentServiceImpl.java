package com.company.ems.document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.company.ems.company.CompanyRepository;
import com.company.ems.documentType.DocumentType;
import com.company.ems.documentType.DocumentTypeRepository;
import com.company.ems.security.jwt.SecurityUtil;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentMapper documentMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;

    public DocumentServiceImpl(DocumentRepository documentRepository, 
                               DocumentTypeRepository documentTypeRepository, 
                               DocumentMapper documentMapper,
                               CompanyRepository companyRepository,
                               SecurityUtil securityUtil) {
        this.documentRepository = documentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentMapper = documentMapper;
        this.companyRepository = companyRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(UUID id) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
    	Document document = documentRepository.findActiveByIdAndCompanyId(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Active document record not found or unauthorized access."));
        return documentMapper.toDto(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getAllActiveDocuments() {
UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        List<Document> documents = documentRepository.findAllActiveDocumentsByCompanyId(tenantCompanyId);
        return documentMapper.toDtoList(documents);
    }

    @Override
    @Transactional
    public DocumentDto createDocument(MultipartFile file, UUID documentTypeId) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();        
        DocumentType docType = documentTypeRepository.findByIdActiveAndTenantScope(documentTypeId, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Document Type not found or unauthorized"));
                
        String originalName = file.getOriginalFilename();
        String sanitizedFileName = originalName;
        if (originalName != null && originalName.contains(".")) {
            int lastDot = originalName.lastIndexOf(".");
            String nameWithoutExt = originalName.substring(0, lastDot);
            String extension = originalName.substring(lastDot);
            String cleanName = nameWithoutExt.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            sanitizedFileName = cleanName + extension;
        } else if (originalName != null) {
            sanitizedFileName = originalName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        }
        String fileName = System.currentTimeMillis() + "_" + sanitizedFileName;
        String uploadDir = System.getProperty("user.dir") + "/uploads/documents/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(uploadDir + fileName));
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
        
        Document document = new Document();
        document.setFileName(sanitizedFileName);
        document.setFilePath("/uploads/documents/" + fileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setDocumentType(docType);       
        document.setCompany(companyRepository.getReferenceById(tenantCompanyId));
        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDto(savedDocument);
    }

    @Override
    @Transactional
    public DocumentDto updateDocument(UUID id, EditDocumentDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Document document = documentRepository.findActiveByIdAndCompanyId(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Active document metadata not found or unauthorized access."));
        DocumentType docType = documentTypeRepository.findByIdActiveAndTenantScope(dto.getDocumentTypeId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target Document Type classification not found or unauthorized."));
        if (document.getCompany() == null || !document.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized modification attempt.");
        }        
        documentMapper.updateEntityFromDto(dto, document);
        document.setDocumentType(docType);
        Document updatedDocument = documentRepository.save(document);
        return documentMapper.toDto(updatedDocument);
    }

    @Override
    @Transactional
    public void deleteDocument(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Document document = documentRepository.findActiveByIdAndCompanyId(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Active document record not found to delete or unauthorized access."));      
        if (document.getCompany() == null || !document.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized delete attempt.");
        }
        document.setDeleted(true);
        documentRepository.save(document);
    }
}
