package com.company.ems.qualificationType;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.company.CompanyRepository;
import com.company.ems.security.jwt.SecurityUtil;

import java.util.List;
import java.util.UUID;

@Service
public class QualificationTypeServiceImpl implements QualificationTypeService {

    private final QualificationTypeRepository qualificationTypeRepository;
    private final QualificationTypeMapper qualificationTypeMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;

    public QualificationTypeServiceImpl(QualificationTypeRepository qualificationTypeRepository, 
                                         QualificationTypeMapper qualificationTypeMapper,
                                         CompanyRepository companyRepository,
                                         SecurityUtil securityUtil) {
        this.qualificationTypeRepository = qualificationTypeRepository;
        this.qualificationTypeMapper = qualificationTypeMapper;
        this.companyRepository = companyRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public QualificationTypeDto createQualificationType(CreateQualificationTypeDto createDto) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        String trimmedName = createDto.getName().trim();
        qualificationTypeRepository.findByNameActiveAndTenantScope(trimmedName, tenantCompanyId)
                .ifPresent(q -> { 
                    throw new IllegalArgumentException("A qualification type with name '" + trimmedName + "' already exists."); 
                });

        QualificationType entity = qualificationTypeMapper.toEntity(createDto);
        entity.setName(trimmedName);
        entity.setCompany(companyRepository.getReferenceById(tenantCompanyId));      
        QualificationType saved = qualificationTypeRepository.save(entity);
        return qualificationTypeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualificationTypeDto> getAllActiveQualificationTypes() {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        List<QualificationType> activeList = qualificationTypeRepository.findAllActiveAndTenantScope(tenantCompanyId);
        return qualificationTypeMapper.toDtoList(activeList);
    }

    @Override
    @Transactional(readOnly = true)
    public QualificationTypeDto getQualificationTypeById(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        QualificationType entity = qualificationTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Qualification type not found or unauthorized access."));
        return qualificationTypeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public QualificationTypeDto updateQualificationType(UUID id, EditQualificationTypeDto editDto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();      
        QualificationType entity = qualificationTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Qualification type not found or unauthorized access."));

        String trimmedName = editDto.getName().trim();
        if (entity.getCompany() == null || !entity.getCompany().getId().equals(tenantCompanyId)) {
            throw new IllegalArgumentException("System default qualification types cannot be modified. Please create a new custom type instead.");
        }
        if (!entity.getName().equalsIgnoreCase(trimmedName)) {
            qualificationTypeRepository.findByNameActiveAndTenantScope(trimmedName, tenantCompanyId)
                    .ifPresent(q -> { 
                        throw new IllegalArgumentException("Another active qualification type with name '" + trimmedName + "' already exists."); 
                    });
        }
        qualificationTypeMapper.updateEntityFromDto(editDto, entity);
        entity.setName(trimmedName);     
        QualificationType updated = qualificationTypeRepository.save(entity);
        return qualificationTypeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteQualificationType(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        QualificationType entity = qualificationTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Qualification type not found or unauthorized access."));
        if (entity.getCompany() == null || !entity.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized delete attempt. System default configurations cannot be deleted.");
        }
        entity.setDeleted(true);
        qualificationTypeRepository.save(entity);
    }
}
