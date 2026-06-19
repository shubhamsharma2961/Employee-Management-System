package com.company.ems.department;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.company.CompanyRepository;
import com.company.ems.security.jwt.SecurityUtil;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;
    
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, CompanyRepository companyRepository, SecurityUtil securityUtil) {
    	this.departmentMapper = departmentMapper;
    	this.departmentRepository = departmentRepository;
    	this.companyRepository = companyRepository;
    	this.securityUtil = securityUtil; 	
    }

    @Override
    @Transactional
    public DepartmentDto createDepartment(CreateDepartmentDto dto) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        String formattedCode = dto.getCode().trim().toUpperCase();
        String trimmedName = dto.getName().trim();       
        if (departmentRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Department name '" + dto.getName() + "' is already in use by your organization.");
        }
        if (departmentRepository.existsByCodeAndCompanyIdAndIsDeletedFalse(formattedCode, tenantCompanyId)) {
            throw new IllegalArgumentException("Department code '" + formattedCode + "' is already in use by your organization.");
        }
        Department department = departmentMapper.toEntity(dto);
        department.setCode(formattedCode); 
        department.setCompany(companyRepository.getReferenceById(tenantCompanyId));
        return departmentMapper.toDto(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartment(UUID id, EditDepartmentDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Department existing = departmentRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Department not found or has been soft-deleted."));
        String formattedCode = dto.getCode().trim().toUpperCase();
        String trimmedName = dto.getName().trim();        
        if (existing.getCompany() == null || !existing.getCompany().getId().equals(tenantCompanyId)) {
            throw new IllegalArgumentException("System default and global departments cannot be modified. Please create a new custom department instead.");
        }
        if (!existing.getName().equalsIgnoreCase(trimmedName) && 
            departmentRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Department name '" + dto.getName() + "' is already in use.");
        }
        if (!existing.getCode().equalsIgnoreCase(formattedCode) && 
            departmentRepository.existsByCodeAndCompanyIdAndIsDeletedFalse(formattedCode, tenantCompanyId)) {
            throw new IllegalArgumentException("Department code '" + formattedCode + "' is already in use.");
        }
        departmentMapper.updateEntityFromDto(dto, existing);
        existing.setCode(formattedCode);       
        return departmentMapper.toDto(departmentRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDto> getDepartments(String search, Pageable pageable) {
    	return departmentRepository.searchActiveDepartmentsByTenant(search, securityUtil.getCurrentCompanyId(), pageable)
                .map(departmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(UUID id) {
    	return departmentRepository.findByIdActiveAndTenantScope(id, securityUtil.getCurrentCompanyId())
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Department not found or access denied."));
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Department department = departmentRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Department not found or already soft-deleted."));
        if (department.getCompany() == null) {
            throw new IllegalArgumentException("System default departments cannot be deleted by tenants.");
        }
        if (!department.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized deletion attempt.");
        }
        department.setDeleted(true);
        departmentRepository.save(department);
    }
}
