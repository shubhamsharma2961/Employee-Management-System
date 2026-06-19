package com.company.ems.userrole;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.user.UserDto;
import com.company.ems.user.UserService;
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-roles")
public class UserRoleController {
	private final UserRoleService userRoleService;
	private final UserService userService;

    public UserRoleController(UserRoleService userRoleService, UserService userService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user-role:create')")
    public ResponseEntity<ApiResponse<List<UserRoleDto>>> assignUserRole(@RequestBody @Valid AssignUserRoleDto dto) {
        List<UserRoleDto> result = userRoleService.assignUserRole(dto);
        ApiResponse<List<UserRoleDto>> response = new ApiResponse<>(true, "Roles assigned to user successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user-role:read')")
    public ResponseEntity<ApiResponse<List<UserRoleDto>>> getRolesByUserId(@PathVariable UUID userId) {
        List<UserRoleDto> result = userRoleService.getRolesByUserId(userId);
        ApiResponse<List<UserRoleDto>> response = new ApiResponse<>(true, "User roles fetched successfully", result);
        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user-role:read')")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsersByEmail(@RequestParam String email) {
        List<UserDto> result = userService.searchByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", result));
    }
    
    @PutMapping
    @PreAuthorize("hasAuthority('user-role:update')")
    public ResponseEntity<ApiResponse<List<UserRoleDto>>> replaceUserRoles(@RequestBody @Valid AssignUserRoleDto dto) {
        List<UserRoleDto> result = userRoleService.replaceUserRoles(dto);
        ApiResponse<List<UserRoleDto>> response = new ApiResponse<>(true, "User roles updated successfully", result);
        return ResponseEntity.ok(response); 
    }

}
