package com.company.ems.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
    @Mapping(target = "roles", expression = "java(user.getUserRoles() != null ? user.getUserRoles().stream().map(ur -> ur.getRole().getName()).collect(java.util.stream.Collectors.toList()) : null)")
    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "passwordChanged", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserDto dto);
}
