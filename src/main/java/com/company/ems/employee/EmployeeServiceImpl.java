package com.company.ems.employee;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.company.ems.common.DataScope;
import com.company.ems.common.UserStatus;
import com.company.ems.company.Company;
import com.company.ems.company.CompanyRepository;
import com.company.ems.department.DepartmentRepository;
import com.company.ems.designation.DesignationRepository;
import com.company.ems.employeeType.EmployeeTypeRepository;
import com.company.ems.role.Role;
import com.company.ems.role.RoleRepository;
import com.company.ems.security.jwt.DataScopeService;
import com.company.ems.security.jwt.SecurityUtil;
import com.company.ems.security.jwt.UserPrincipal;
import com.company.ems.user.User;
import com.company.ems.user.UserRepository;
import com.company.ems.userrole.UserRole;
import com.company.ems.userrole.UserRoleRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final EmployeeTypeRepository employeeTypeRepository;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    
    private final DataScopeService dataScopeService;
    private final CompanyRepository companyRepository;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
			DepartmentRepository departmentRepository, DesignationRepository designationRepository,
			EmployeeTypeRepository employeeTypeRepository, UserRepository userRepository, RoleRepository roleRepository,
			UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, DataScopeService dataScopeService, 
			CompanyRepository companyRepository, SecurityUtil securityUtil) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.employeeTypeRepository = employeeTypeRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
		this.dataScopeService = dataScopeService;
		this.companyRepository = companyRepository;
		this.passwordEncoder = passwordEncoder;
		this.securityUtil = securityUtil;
	}
    
    @Override
    public EmployeeDto getMyProfile() {
    	UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID employeeId = principal.getEmployeeId();
        if (employeeId == null) {
            throw new RuntimeException("Current user session context is not linked to any active employee record.");
        }
        Employee employee = employeeRepository.findByIdAndCompanyId(employeeId, principal.getCompanyId())
                .filter(emp -> !emp.isDeleted())
                .orElseThrow(() -> new RuntimeException("Employee profile not found or has been soft-deleted."));                
        return employeeMapper.toDto(employee);
    }

	@Override
    @Transactional
    public EmployeeDto createEmployee(CreateEmployeeDto dto, UserPrincipal currentUser) {		
		UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        if (tenantCompanyId == null) {
            throw new RuntimeException("Current user's company context is missing from token structural properties");
        }       
        Company tenantCompany = companyRepository.getReferenceById(tenantCompanyId);
        Employee employee = employeeMapper.toEntity(dto);
        employee.setCompany(tenantCompany);
        
        employee.setDepartment(departmentRepository.findByIdActiveAndTenantScope(dto.getDepartmentId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target department not found or access denied across organization pools.")));
        employee.setDesignation(designationRepository.findByIdActiveAndTenantScope(dto.getDesignationId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target designation not found or access denied across organization pools.")));
        employee.setEmployeeType(employeeTypeRepository.findByIdActiveAndTenantScope(dto.getEmployeeTypeId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target employee type not found or access denied across organization pools.")));
        if (dto.getReportingManagerId() != null) {
            employee.setReportingManager(
                    employeeRepository.findByIdAndCompanyId(dto.getReportingManagerId(), tenantCompanyId)
                            .filter(mgr -> !mgr.isDeleted())
                            .orElseThrow(() -> new RuntimeException("Active reporting manager profile not found within your organization."))
            );
        }

        User user = new User();
        user.setEmail(dto.getEmail());
//        user.setCompany(tenantCompany); 
        user.setPassword(passwordEncoder.encode(dto.getPhoneNumber()));
        user.setStatus(UserStatus.LOCKED);
        user.setPasswordChanged(false);
        user = userRepository.save(user);

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Assigned security role configuration profile not found."));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);     
        user.getUserRoles().add(userRole);
        
        employee.setUser(user);
        
        Long nextSequenceValue = employeeRepository.getNextEmployeeSequenceValue();
        String formattedCode = String.format("EMP-%05d", nextSequenceValue);
        employee.setEmployeeCode(formattedCode);
        
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }

	@Override
	@Transactional(readOnly = true)
	public EmployeeDto getEmployeeById(UUID id) {
		UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();

        Employee employee = employeeRepository.findEmployeeWithDetailsByIdAndCompanyId(id, tenantCompanyId)
                .filter(emp -> !emp.isDeleted())
                .orElseThrow(() -> new RuntimeException("Employee record not found within your organization."));
	    
	    DataScope scope = dataScopeService.getCurrentUserScope();
	    switch (scope) {
        case ALL -> {
            return employeeMapper.toDto(employee);
        }
        case DEPARTMENT -> {
            UUID userDepartmentId = principal.getDepartmentId();
            if (employee.getDepartment() == null || !employee.getDepartment().getId().equals(userDepartmentId)) {
                throw new AccessDeniedException("Access denied: Target profile belongs to an unauthorized department branch.");
            }
            return employeeMapper.toDto(employee);
        }
        case SELF -> {
            UUID loggedInEmployeeId = principal.getEmployeeId();
            if (!id.equals(loggedInEmployeeId)) {
                throw new AccessDeniedException("Access denied: Your operational clearance is restricted to your individual profile.");
            }
            return employeeMapper.toDto(employee);
        }
        default -> throw new RuntimeException("Unsupported or corrupted data access scope structure.");
	    }
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EmployeeDto> getAllEmployees(EmployeeSearchCriteria criteria, Pageable pageable) {
		UserPrincipal principal = securityUtil.getCurrentPrincipal();
        UUID tenantCompanyId = principal.getCompanyId();	           
	    DataScope scope = dataScopeService.getCurrentUserScope();
	    switch (scope) {
        case ALL -> {
            return employeeRepository.searchEmployeesTenantScoped(
                    criteria.getSearch(), criteria.getDepartmentId(), criteria.getDesignationId(), criteria.getEmployeeTypeId(), tenantCompanyId, pageable
            ).map(employeeMapper::toDto);
        }
        case DEPARTMENT -> {
            UUID userDeptId = principal.getDepartmentId();
            
            if (criteria.getDepartmentId() != null && !criteria.getDepartmentId().equals(userDeptId)) {
                throw new AccessDeniedException("Access denied: Department filtration payload falls outside your scope.");
            }

            return employeeRepository.searchEmployeesTenantScoped(
                    criteria.getSearch(), userDeptId, criteria.getDesignationId(), criteria.getEmployeeTypeId(), tenantCompanyId, pageable
            ).map(employeeMapper::toDto);
        }
        case SELF -> {
            if (pageable.getOffset() > 0) {
                return new PageImpl<>(Collections.emptyList(), pageable, 1);
            }
            Employee employee = employeeRepository.findByIdAndCompanyId(principal.getEmployeeId(), tenantCompanyId)
                    .orElseThrow(() -> new RuntimeException("Profile row missing or corrupted."));
                    
            return new PageImpl<>(List.of(employeeMapper.toDto(employee)), pageable, 1);
        }
        default -> throw new RuntimeException("Unsupported or invalid data access scope structure.");
	    }
	}

	@Override
    @Transactional
    public EmployeeDto updateEmployee(UUID id, EditEmployeeDto dto) {
        UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Employee employee = employeeRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(emp -> !emp.isDeleted())
                .orElseThrow(() -> new RuntimeException("Employee record not found or access denied across organization pools."));
        
        employeeMapper.updateEmployeeFromDto(dto, employee);
        employee.setDepartment(departmentRepository.findByIdActiveAndTenantScope(dto.getDepartmentId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target department not found or access denied across organization pools.")));
        employee.setDesignation(designationRepository.findByIdActiveAndTenantScope(dto.getDesignationId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target designation not found or access denied across organization pools.")));
        employee.setEmployeeType(employeeTypeRepository.findByIdActiveAndTenantScope(dto.getEmployeeTypeId(), tenantCompanyId)
                .orElseThrow(() -> new RuntimeException("Target employee type not found or access denied across organization pools.")));
        
        if (dto.getReportingManagerId() != null) {
            employee.setReportingManager(
                    employeeRepository.findByIdAndCompanyId(dto.getReportingManagerId(), tenantCompanyId)
                            .filter(mgr -> !mgr.isDeleted())
                            .orElseThrow(() -> new RuntimeException("Active reporting manager profile not found."))
            );
        } else {
            employee.setReportingManager(null);
        }
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
    	UUID tenantCompanyId = securityUtil.getCurrentCompanyId();
        Employee employee = employeeRepository.findByIdAndCompanyId(id, tenantCompanyId)
                .filter(emp -> !emp.isDeleted())
                .orElseThrow(() -> new RuntimeException("Employee record not found or access denied across organization pools."));
        employee.setDeleted(true);
        if (employee.getUser() != null) {
        	User user = employee.getUser();
            user.setStatus(UserStatus.INACTIVE);
            user.setDeleted(true);
            userRepository.save(user);
        }
        employeeRepository.save(employee);
    }

}
