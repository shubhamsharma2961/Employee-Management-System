package com.company.ems.employeeType;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ems.company.CompanyRepository;
import com.company.ems.security.jwt.SecurityUtil;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeTypeServiceImpl implements EmployeeTypeService {

    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeMapper employeeTypeMapper;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;

    public EmployeeTypeServiceImpl(EmployeeTypeRepository employeeTypeRepository, EmployeeTypeMapper employeeTypeMapper, CompanyRepository companyRepository, SecurityUtil securityUtil) {
        this.employeeTypeRepository = employeeTypeRepository;
        this.employeeTypeMapper = employeeTypeMapper;
        this.companyRepository = companyRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public EmployeeTypeDto createEmployeeType(CreateEmployeeTypeDto createDto) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        String trimmedName = createDto.getName().trim();
        if (employeeTypeRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("An active employee type named '" + trimmedName + "' already exists.");
        }
        EmployeeType entity = employeeTypeMapper.toEntity(createDto);
        entity.setName(trimmedName);
        entity.setCompany(companyRepository.getReferenceById(tenantCompanyId));     
        EmployeeType saved = employeeTypeRepository.save(entity);
        return employeeTypeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeTypeDto> getAllActiveEmployeeTypes() {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
    	List<EmployeeType> activeTypes = employeeTypeRepository.findAllActiveAndTenantScope(tenantCompanyId);
        return employeeTypeMapper.toDtoList(activeTypes);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeTypeDto getEmployeeTypeById(UUID id) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
    	EmployeeType entity = employeeTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Employee Type not found or unauthorized access."));
        return employeeTypeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public EmployeeTypeDto updateEmployeeType(UUID id, EditEmployeeTypeDto editDto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();       
        EmployeeType entity = employeeTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Employee Type not found or unauthorized access."));
        String trimmedName = editDto.getName().trim();
        if (entity.getCompany() == null || !entity.getCompany().getId().equals(tenantCompanyId)) {
            throw new IllegalArgumentException("System default configurations cannot be modified. Please create a new custom employee type instead.");
        }
        if (!entity.getName().equalsIgnoreCase(trimmedName) && 
            employeeTypeRepository.existsByNameAndCompanyIdAndIsDeletedFalse(trimmedName, tenantCompanyId)) {
            throw new IllegalArgumentException("Employee type name '" + trimmedName + "' is already in use.");
        }
        employeeTypeMapper.updateEntityFromDto(editDto, entity);
        entity.setName(trimmedName);
        EmployeeType updated = employeeTypeRepository.save(entity);
        return employeeTypeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteEmployeeType(UUID id) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();       
        EmployeeType entity = employeeTypeRepository.findByIdActiveAndTenantScope(id, tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Employee Type not found or unauthorized access."));
        if (entity.getCompany() == null || !entity.getCompany().getId().equals(tenantCompanyId)) {
            throw new AccessDeniedException("Unauthorized delete attempt. System default configurations cannot be deleted.");
        }
        entity.setDeleted(true);
        employeeTypeRepository.save(entity);
    }
}