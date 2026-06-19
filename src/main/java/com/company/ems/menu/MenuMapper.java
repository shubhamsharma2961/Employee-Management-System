package com.company.ems.menu;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuMapper {
	
    Menu toEntity(CreateMenuDto dto);
    
    @Mapping(source = "parent.id", target = "parentId")
    MenuDto toDto(Menu entity);

    List<MenuDto> toDtoList(List<Menu> entities);

    void updateEntityFromDto(EditMenuDto dto, @MappingTarget Menu entity);
}
