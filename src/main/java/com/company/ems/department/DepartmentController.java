package com.company.ems.department;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('department:create')")
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(@RequestBody @Valid CreateDepartmentDto dto) {
        DepartmentDto result = departmentService.createDepartment(dto);
        ApiResponse<DepartmentDto> response = new ApiResponse<>(true, "Department created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('department:update')")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(@PathVariable UUID id, @RequestBody @Valid EditDepartmentDto dto) {
        DepartmentDto result = departmentService.updateDepartment(id, dto);
        ApiResponse<DepartmentDto> response = new ApiResponse<>(true, "Department updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('department:read')")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable UUID id) {
        DepartmentDto result = departmentService.getDepartmentById(id);
        ApiResponse<DepartmentDto> response = new ApiResponse<>(true, "Department details fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('department:read', 'department:lookup')")
    public ResponseEntity<ApiResponse<Page<DepartmentDto>>> getAllDepartments(@RequestParam(value = "search", required = false) String search, @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<DepartmentDto> result = departmentService.getDepartments(search, pageable);
        ApiResponse<Page<DepartmentDto>> response = new ApiResponse<>(true, "System departments fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('department:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Department soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
