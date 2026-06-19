package com.company.ems.qualification;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.company.ems.document.DocumentMapper;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface QualificationMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.fullName", target = "employeeName")
    @Mapping(source = "qualificationType.id", target = "qualificationTypeId")
    @Mapping(source = "qualificationType.name", target = "qualificationTypeName")
    @Mapping(source = "documents", target = "documents")
    QualificationDto toDto(Qualification qualification);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "qualificationType", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "status", ignore = true) 
    Qualification toEntity(CreateQualificationDto dto);

    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "qualificationType", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "status", ignore = true) 
    void updateEntityFromDto(EditQualificationDto dto, @MappingTarget Qualification qualification);

    List<QualificationDto> toDtoList(List<Qualification> qualifications);
}