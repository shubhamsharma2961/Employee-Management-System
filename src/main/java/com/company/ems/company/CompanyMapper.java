package com.company.ems.company;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDto toDto(Company company);
    
    List<CompanyDto> toDtoList(List<Company> companies);
    
    Company toEntity(CreateCompanyDto createCompanyDto);
    
    List<Company> toEntityList(List<CreateCompanyDto> createCompanyDtos);
    
    void updateEntityFromDto(EditCompanyDto editCompanyDto, @MappingTarget Company company);

}
