package com.company.ems.employee;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.company.ems.security.jwt.UserPrincipal;

public interface EmployeeService {
	EmployeeDto createEmployee(CreateEmployeeDto dto, UserPrincipal currentUser);
    EmployeeDto getEmployeeById(UUID id);
    Page<EmployeeDto> getAllEmployees(
    		EmployeeSearchCriteria criteria,
            Pageable pageable
    );
    EmployeeDto updateEmployee(UUID id, EditEmployeeDto dto);
    void deleteEmployee(UUID id);
	EmployeeDto getMyProfile();
}
