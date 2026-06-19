package com.company.ems.employee;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.company.ems.security.jwt.UserPrincipal;
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('employee:read_own')")
    public ResponseEntity<ApiResponse<EmployeeDto>> getMyProfile() {
        EmployeeDto result = employeeService.getMyProfile();
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", result));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('employee:create')")
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@RequestBody @Valid CreateEmployeeDto dto, @AuthenticationPrincipal UserPrincipal currentUser) {
        EmployeeDto result = employeeService.createEmployee(dto, currentUser);
        return new ResponseEntity<>(new ApiResponse<>(true, "Employee created successfully", result),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeById(@PathVariable UUID id) {
        EmployeeDto result = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee fetched successfully", result));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('employee:read')")
    public ResponseEntity<ApiResponse<Page<EmployeeDto>>> getAllEmployees(EmployeeSearchCriteria criteria, Pageable pageable) {
        Page<EmployeeDto> result = employeeService.getAllEmployees(criteria, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employees fetched successfully", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('employee:update')")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(@PathVariable UUID id, @RequestBody @Valid EditEmployeeDto dto) {
        EmployeeDto result = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee updated successfully", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('employee:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee deleted successfully", null));
    }
}