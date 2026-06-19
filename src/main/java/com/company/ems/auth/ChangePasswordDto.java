package com.company.ems.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
	    @NotBlank String currentPassword,
	    @NotBlank(message = "New password is required")
	    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long")
	    @Pattern(
	        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).*$",
	        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&#)"
	    )
	    String newPassword,
	    @NotBlank String confirmPassword
	    ) {}
