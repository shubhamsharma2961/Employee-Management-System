package com.company.ems.designation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.company.ems.company.CompanyRepository;
import com.company.ems.security.jwt.SecurityUtil;

import java.util.UUID;

@Service
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil; 

    public DesignationServiceImpl(DesignationRepository designationRepository, 
                                  DesignationMapper designationMapper, 
                                  CompanyRepository companyRepository,
                                  SecurityUtil securityUtil) {
        this.designationRepository = designationRepository;
        this.designationMapper = designationMapper;
        this.companyRepository = companyRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public DesignationDto createDesignation(CreateDesignationDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        String formattedCode = dto.getCode().trim().toUpperCase();
        String trimmedName = dto.getName().trim();
        if (designationRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Designation name '" + dto.getName() + "' is already in use by your organization.");
        }
        if (designationRepository.existsByCodeAndCompanyIdAndIsDeletedFalse(formattedCode, tenantCompanyId)) {
            throw new IllegalArgumentException("Designation code '" + formattedCode + "' is already in use by your organization.");
        }
        Designation designation = designationMapper.toEntity(dto);
        designation.setCode(formattedCode);   
        designation.setCompany(companyRepository.getReferenceById(tenantCompanyId));       
        return designationMapper.toDto(designationRepository.save(designation));
    }

    @Override
    @Transactional
    public DesignationDto updateDesignation(UUID id, EditDesignationDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Designation existing = designationRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Designation not found or has been soft-deleted."));        
        String formattedCode = dto.getCode().trim().toUpperCase();
        String trimmedName = dto.getName().trim();
        if (existing.getCompany() == null || !existing.getCompany().getId().equals(tenantCompanyId)) {
            throw new IllegalArgumentException("System default and global designations cannot be modified. Please create a new custom designation instead.");
        }
        if (!existing.getName().equalsIgnoreCase(trimmedName) && 
            designationRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Designation name '" + dto.getName() + "' is already in use.");
        }
        if (!existing.getCode().equalsIgnoreCase(formattedCode) && 
            designationRepository.existsByCodeAndCompanyIdAndIsDeletedFalse(formattedCode, tenantCompanyId)) {
            throw new IllegalArgumentException("Designation code '" + formattedCode + "' is already in use.");
        }  
        designationMapper.updateEntityFromDto(dto, existing);
        existing.setCode(formattedCode);   
        return designationMapper.toDto(designationRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DesignationDto> getDesignations(String search, Pageable pageable) {
        return designationRepository.searchActiveDesignationsByTenant(search, securityUtil.getCurrentCompanyId(), pageable)
                .map(designationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationDto getDesignationById(UUID id) {
        return designationRepository.findByIdActiveAndTenantScope(id, securityUtil.getCurrentCompanyId())
                .map(designationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Designation not found or access denied."));
    }

    @Override
    @Transactional
    public void deleteDesignation(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Designation designation = designationRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Designation not found or already soft-deleted."));
        if (designation.getCompany() == null) {
            throw new IllegalArgumentException("System default designations cannot be deleted by tenants. " +
                    "If you do not want to use this, please request your admin to deactivate it via your dashboard preferences.");
        }
        if (!designation.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized deletion attempt.");
        }
        designation.setDeleted(true);
        designationRepository.save(designation);
    }
}