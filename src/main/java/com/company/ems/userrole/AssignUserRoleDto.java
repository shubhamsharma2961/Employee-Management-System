package com.company.ems.userrole;

import java.util.List;
import java.util.UUID;

public class AssignUserRoleDto {
	private UUID userId;
    private List<UUID> roleIds; 

    public AssignUserRoleDto() {}
    public AssignUserRoleDto(UUID userId, List<UUID> roleIds) {
        this.userId = userId;
        this.roleIds = roleIds;
    }
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	public List<UUID> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<UUID> roleIds) {
		this.roleIds = roleIds;
	}
   
}
