package com.company.ems.userrole;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.ems.role.RoleMapper;
import com.company.ems.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class})
public interface UserRoleMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    UserRoleDto toDto(UserRole userRole);

    List<UserRoleDto> toDtoList(List<UserRole> userRoles);
}
