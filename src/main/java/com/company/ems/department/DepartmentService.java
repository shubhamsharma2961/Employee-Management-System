package com.company.ems.department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDto createDepartment(CreateDepartmentDto dto);
    DepartmentDto updateDepartment(UUID id, EditDepartmentDto dto);
    Page<DepartmentDto> getDepartments(String search, Pageable pageable);
    DepartmentDto getDepartmentById(UUID id);
    void deleteDepartment(UUID id);
}
