package com.company.ems.user;

import java.util.List;
import java.util.UUID;

import com.company.ems.common.UserStatus;

public class UserDto {
	private UUID id;
    private String email;
    private UserStatus status;
    private Boolean isPasswordChanged;
    private List<String> roles;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public Boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}
	public void setIsPasswordChanged(Boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
    
}
