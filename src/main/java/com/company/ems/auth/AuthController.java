package com.company.ems.auth;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.security.jwt.UserPrincipal;
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtDto>> login(@RequestBody @Valid LoginDto loginDto) {
        ApiResponse<JwtDto> response = new ApiResponse<>(true, "Login successful", authService.login(loginDto));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        authService.changePassword(userPrincipal.getUsername(), changePasswordDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully. You can now access the full system.", null));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserMeResponseDto>> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
        }
        String email = authentication.getName();
        UserMeResponseDto result = authService.getCurrentUserProfile(email);
        ApiResponse<UserMeResponseDto> response = new ApiResponse<>(true, "Profile context initialized successfully", result);
        return ResponseEntity.ok(response);
    }
}