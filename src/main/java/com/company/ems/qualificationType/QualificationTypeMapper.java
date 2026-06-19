package com.company.ems.qualificationType;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QualificationTypeMapper {

    QualificationTypeDto toDto(QualificationType entity);

    List<QualificationTypeDto> toDtoList(List<QualificationType> entities);

    QualificationType toEntity(CreateQualificationTypeDto createDto);

    void updateEntityFromDto(EditQualificationTypeDto editDto, @MappingTarget QualificationType entity);
}
