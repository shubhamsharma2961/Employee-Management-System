package com.company.ems.rolemenu;

import java.util.List;
import java.util.UUID;

import com.company.ems.menu.MenuGroupDto;

public interface RoleMenuService {
	List<RoleMenuDto> assignRoleMenu(AssignRoleMenuDto dto);
	List<RoleMenuDto> updateRoleMenu(AssignRoleMenuDto dto);
    List<RoleMenuDto> getMenusByRoleId(UUID roleId);
	List<MenuGroupDto> getRoleMenuGrouped(UUID roleId);
}
