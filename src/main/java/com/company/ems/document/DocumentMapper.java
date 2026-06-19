package com.company.ems.document;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "documentType.id", target = "documentTypeId")
    @Mapping(source = "documentType.name", target = "documentTypeName")
    DocumentDto toDto(Document document);

    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "company", ignore = true)
    Document toEntity(CreateDocumentDto createDocumentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateEntityFromDto(EditDocumentDto editDocumentDto, @MappingTarget Document document);
    
    List<DocumentDto> toDtoList(List<Document> documents);
}