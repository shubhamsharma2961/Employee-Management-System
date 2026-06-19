package com.company.ems.role;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role entity);

    List<RoleDto> toDtoList(List<Role> entities);
    
    Role toEntity(CreateRoleDto dto);

    void updateEntityFromDto(EditRoleDto dto, @MappingTarget Role entity);
}
