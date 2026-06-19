package com.company.ems.employeeType;

import java.util.List;
import java.util.UUID;

public interface EmployeeTypeService {
    EmployeeTypeDto createEmployeeType(CreateEmployeeTypeDto createDto);
    List<EmployeeTypeDto> getAllActiveEmployeeTypes();
    EmployeeTypeDto getEmployeeTypeById(UUID id);
    EmployeeTypeDto updateEmployeeType(UUID id, EditEmployeeTypeDto editDto);
    void deleteEmployeeType(UUID id);
}
