package com.company.ems.rolemenu;

import java.util.List;
import java.util.UUID;

import com.company.ems.menu.MenuDto;

public class RoleMenuDto {
	private UUID roleId;
	private String roleName;
	private List<MenuDto> assignedMenus;
	public UUID getRoleId() {
		return roleId;
	}
	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<MenuDto> getAssignedMenus() {
		return assignedMenus;
	}
	public void setAssignedMenus(List<MenuDto> assignedMenus) {
		this.assignedMenus = assignedMenus;
	}

}
