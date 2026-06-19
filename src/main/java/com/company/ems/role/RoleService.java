package com.company.ems.role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
	List<RoleDto> getAllRoles();
    RoleDto getRoleById(UUID id);
    RoleDto createRole(CreateRoleDto dto);
    RoleDto updateRole(UUID id, EditRoleDto dto);
    void deleteRole(UUID id);
}
