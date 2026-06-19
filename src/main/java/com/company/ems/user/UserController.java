package com.company.ems.user;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody @Valid CreateUserDto dto) {
        UserDto result = userService.createUser(dto);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User account created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable UUID id, @RequestBody @Valid EditUserDto dto) {
        UserDto result = userService.updateUser(id, dto);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User account updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        UserDto result = userService.getUserById(id);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User record fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> result = userService.getAllUsers();
        ApiResponse<List<UserDto>> response = new ApiResponse<>(true, "All active users fetched successfully", result);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchByEmail(
            @RequestParam String email) {
        List<UserDto> result = userService.searchByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users found", result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "User account soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
