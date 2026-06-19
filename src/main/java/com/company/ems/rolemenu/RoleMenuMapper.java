package com.company.ems.rolemenu;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.company.ems.menu.MenuDto;
import com.company.ems.menu.MenuMapper;

@Mapper(componentModel = "spring", uses = {MenuMapper.class})
public interface RoleMenuMapper {
	@Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", expression = "java(roleMenu.getRole().getName())")
	@Mapping(target = "assignedMenus", ignore = true)
	RoleMenuDto toDto(RoleMenu roleMenu);
	
	List<RoleMenuDto> toDtoList(List<RoleMenu> roleMenus);

    @Mapping(target = "id", source = "menu.id")
    @Mapping(target = "name", source = "menu.name")
    @Mapping(target = "url", source = "menu.url")
    @Mapping(target = "permissionKey", source = "menu.permissionKey")
    @Mapping(target = "icon", source = "menu.icon")
    @Mapping(target = "sortOrder", source = "menu.sortOrder")
    MenuDto roleMenuToMenuDto(RoleMenu roleMenu);
    
    List<MenuDto> roleMenuListToMenuDtoList(List<RoleMenu> roleMenus);

}
