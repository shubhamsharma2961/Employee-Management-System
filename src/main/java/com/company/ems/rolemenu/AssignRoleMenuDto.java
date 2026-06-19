package com.company.ems.rolemenu;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AssignRoleMenuDto {
	@NotNull(message = "Role ID is required")
    private UUID roleId;

    @NotEmpty(message = "At least one Menu ID must be provided")
    private List<UUID> menuIds;

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public List<UUID> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<UUID> menuIds) {
		this.menuIds = menuIds;
	}

}
