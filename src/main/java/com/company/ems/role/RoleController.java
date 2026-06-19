package com.company.ems.role;

import java.util.List;
import java.util.UUID;

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

import com.company.ems.util.ApiResponse;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@RequestBody CreateRoleDto dto) {
        RoleDto result = roleService.createRole(dto);
        ApiResponse<RoleDto> response = new ApiResponse<>(true, "Role created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('role:read', 'role:lookup')")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        List<RoleDto> result = roleService.getAllRoles();
        ApiResponse<List<RoleDto>> response = new ApiResponse<>(true, "System roles fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable UUID id) {
        RoleDto result = roleService.getRoleById(id);
        ApiResponse<RoleDto> response = new ApiResponse<>(true, "Role configuration fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@PathVariable UUID id, @RequestBody EditRoleDto dto) {
        RoleDto result = roleService.updateRole(id, dto);
        ApiResponse<RoleDto> response = new ApiResponse<>(true, "Role updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Role soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}