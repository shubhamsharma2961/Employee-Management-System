package com.company.ems.userrole;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {
	List<UserRoleDto> assignUserRole(AssignUserRoleDto dto);
	List<UserRoleDto> getRolesByUserId(UUID userId);
	List<UserRoleDto> replaceUserRoles(AssignUserRoleDto dto);
}
