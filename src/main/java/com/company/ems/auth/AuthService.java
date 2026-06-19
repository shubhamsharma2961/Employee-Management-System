package com.company.ems.auth;

public interface AuthService {
	JwtDto login(LoginDto request);
    void changePassword(String email, ChangePasswordDto request);
    UserMeResponseDto getCurrentUserProfile(String email);
}
