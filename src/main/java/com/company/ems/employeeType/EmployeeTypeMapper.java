package com.company.ems.employeeType;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeTypeMapper {

    EmployeeTypeDto toDto(EmployeeType entity);

    List<EmployeeTypeDto> toDtoList(List<EmployeeType> entities);

    EmployeeType toEntity(CreateEmployeeTypeDto createDto);

    void updateEntityFromDto(EditEmployeeTypeDto editDto, @MappingTarget EmployeeType entity);
}