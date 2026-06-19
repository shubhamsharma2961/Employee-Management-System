package com.company.ems.rolemenu;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.company.ems.menu.MenuGroupDto;
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/role-menus")
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    public RoleMenuController(RoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role-menu:create')")
    public ResponseEntity<ApiResponse<List<RoleMenuDto>>> assignRoleMenu(@RequestBody @Valid AssignRoleMenuDto dto) {
        List<RoleMenuDto> result = roleMenuService.assignRoleMenu(dto);
        ApiResponse<List<RoleMenuDto>> response = new ApiResponse<>(true, "Permissions assigned to role successfully", result);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping
    @PreAuthorize("hasAuthority('role-menu:update')")
    public ResponseEntity<ApiResponse<List<RoleMenuDto>>> updateRoleMenu(@RequestBody @Valid AssignRoleMenuDto dto) {
        List<RoleMenuDto> result = roleMenuService.updateRoleMenu(dto);
        ApiResponse<List<RoleMenuDto>> response = new ApiResponse<>(true, "Role menu configurations updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('role-menu:read')")
    public ResponseEntity<ApiResponse<List<RoleMenuDto>>> getMenusByRoleId(@PathVariable UUID roleId) {
        List<RoleMenuDto> result = roleMenuService.getMenusByRoleId(roleId);
        ApiResponse<List<RoleMenuDto>> response = new ApiResponse<>(true, "Role configurations fetched successfully", result);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{roleId}/grouped")
    public ResponseEntity<ApiResponse<List<MenuGroupDto>>> getGroupedMenus(@PathVariable UUID roleId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Grouped role menus fetched", roleMenuService.getRoleMenuGrouped(roleId)));
    }
}