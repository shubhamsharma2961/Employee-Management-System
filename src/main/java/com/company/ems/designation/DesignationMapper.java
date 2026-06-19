package com.company.ems.designation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DesignationMapper {

    Designation toEntity(CreateDesignationDto dto);

    DesignationDto toDto(Designation entity);

    List<DesignationDto> toDtoList(List<Designation> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(EditDesignationDto dto, @MappingTarget Designation entity);
}