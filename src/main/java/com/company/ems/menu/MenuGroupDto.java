package com.company.ems.menu;

import java.util.List;

public class MenuGroupDto {
	private String menuName;
    private List<MenuPermissionDto> permissions;
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public List<MenuPermissionDto> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<MenuPermissionDto> permissions) {
		this.permissions = permissions;
	}
    
}
