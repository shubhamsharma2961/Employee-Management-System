package com.company.ems.employeeType;

import com.company.ems.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee-types")
public class EmployeeTypeController {

    private final EmployeeTypeService employeeTypeService;

    public EmployeeTypeController(EmployeeTypeService employeeTypeService) {
        this.employeeTypeService = employeeTypeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('employee-type:create')")
    public ResponseEntity<ApiResponse<EmployeeTypeDto>> create(@Valid @RequestBody CreateEmployeeTypeDto createDto) {
        EmployeeTypeDto data = employeeTypeService.createEmployeeType(createDto);
        ApiResponse<EmployeeTypeDto> response = new ApiResponse<>(true, "Employee type created successfully.", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('employee-type:read', 'employee-type:lookup')")
    public ResponseEntity<ApiResponse<List<EmployeeTypeDto>>> getAllActive() {
        List<EmployeeTypeDto> data = employeeTypeService.getAllActiveEmployeeTypes();
        ApiResponse<List<EmployeeTypeDto>> response = new ApiResponse<>(true, "Active employee types retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('employee-type:read')")
    public ResponseEntity<ApiResponse<EmployeeTypeDto>> getById(@PathVariable UUID id) {
        EmployeeTypeDto data = employeeTypeService.getEmployeeTypeById(id);
        ApiResponse<EmployeeTypeDto> response = new ApiResponse<>(true, "Employee type retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('employee-type:update')")
    public ResponseEntity<ApiResponse<EmployeeTypeDto>> update(@PathVariable UUID id, @Valid @RequestBody EditEmployeeTypeDto editDto) {
        EmployeeTypeDto data = employeeTypeService.updateEmployeeType(id, editDto);
        ApiResponse<EmployeeTypeDto> response = new ApiResponse<>(true, "Employee type updated successfully.", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('employee-type:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteSoft(@PathVariable UUID id) {
        employeeTypeService.deleteEmployeeType(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Employee type deleted successfully.", null);
        return ResponseEntity.ok(response);
    }
}