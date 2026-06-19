package com.company.ems.documentType;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.company.ems.company.CompanyRepository;
import com.company.ems.security.jwt.SecurityUtil;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

	private final DocumentTypeRepository documentTypeRepository;
    private final DocumentTypeMapper documentTypeMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository, 
                                   DocumentTypeMapper documentTypeMapper,
                                   CompanyRepository companyRepository,
                                   SecurityUtil securityUtil) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
        this.companyRepository = companyRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public DocumentTypeDto createDocumentType(CreateDocumentTypeDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        String trimmedName = dto.getName().trim();
        if (documentTypeRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Document type name '" + dto.getName() + "' is already in use by your organization.");
        } 
        DocumentType documentType = documentTypeMapper.toEntity(dto);
        documentType.setName(trimmedName);
        documentType.setCompany(companyRepository.getReferenceById(tenantCompanyId));        
        return documentTypeMapper.toDto(documentTypeRepository.save(documentType));
    }

    @Override
    @Transactional
    public DocumentTypeDto updateDocumentType(UUID id, EditDocumentTypeDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();        
        DocumentType existing = documentTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Document type not found or has been soft-deleted."));              
        String trimmedName = dto.getName().trim();
        if (existing.getCompany() == null || !existing.getCompany().getId().equals(tenantCompanyId)) {
            throw new IllegalArgumentException("System default and global templates cannot be modified. Please create a new custom document type instead.");
        }
        if (!existing.getName().equalsIgnoreCase(trimmedName) && 
            documentTypeRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Document type name '" + dto.getName() + "' is already in use.");
        }                
        documentTypeMapper.updateEntityFromDto(dto, existing);
        existing.setName(trimmedName);   
        return documentTypeMapper.toDto(documentTypeRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeDto> getAllActiveDocumentTypes() {
        List<DocumentType> activeList = documentTypeRepository.searchActiveDocumentTypesByTenant("", securityUtil.getCurrentCompanyId());
        return documentTypeMapper.toDtoList(activeList);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DocumentTypeDto getDocumentTypeById(UUID id) {
        return documentTypeRepository.findByIdActiveAndTenantScope(id, securityUtil.getCurrentCompanyId())
                .map(documentTypeMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Document type not found or access denied."));
    }

    @Override
    @Transactional
    public void deleteDocumentType(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        DocumentType documentType = documentTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Document type not found or already soft-deleted."));

        if (documentType.getCompany() == null) {
            throw new IllegalArgumentException("System default document types cannot be deleted by tenants. " +
                    "If you do not want to use this, please request your admin to deactivate it via your dashboard preferences.");
        }
        if (!documentType.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized deletion attempt.");
        }
        documentType.setDeleted(true);
        documentTypeRepository.save(documentType);
    }
}
