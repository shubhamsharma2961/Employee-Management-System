package com.company.ems.auth;

import java.util.List;
import java.util.UUID;

import com.company.ems.common.UserStatus;
import com.company.ems.menu.MenuDto;

public class UserMeResponseDto {
	private UUID id;
    private String email;
    private UserStatus status;
    private boolean isPasswordChanged;
    private List<String> roles;
    private List<MenuDto> menus;
    private List<String> permissions;
    private String dataScope;  
    private UUID departmentId;
    private UUID companyId;
    private String companyName;
    
	public UUID getCompanyId() {
		return companyId;
	}
	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDataScope() {
		return dataScope;
	}
	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}
	public UUID getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<MenuDto> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuDto> menus) {
		this.menus = menus;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
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
	public boolean isPasswordChanged() {
		return isPasswordChanged;
	}
	public void setPasswordChanged(boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}
//	public List<RoleMenuDto> getRoleMenus() {
//		return roleMenus;
//	}
//	public void setRoleMenus(List<RoleMenuDto> roleMenus) {
//		this.roleMenus = roleMenus;
//	}
    
}
