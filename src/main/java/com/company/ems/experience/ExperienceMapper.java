package com.company.ems.experience;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.company.ems.document.DocumentMapper;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface ExperienceMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "documents", target = "documents")
    ExperienceDto toDto(Experience experience);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "status", ignore = true) 
    Experience toEntity(CreateExperienceDto dto);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "status", ignore = true) 
    void updateEntityFromDto(EditExperienceDto dto, @MappingTarget Experience experience);

    List<ExperienceDto> toDtoList(List<Experience> experiences);
}
