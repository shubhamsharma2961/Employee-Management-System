package com.company.ems.documentType;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DocumentTypeMapper {
    DocumentTypeDto toDto(DocumentType entity);
    
    List<DocumentTypeDto> toDtoList(List<DocumentType> entities);
    
    DocumentType toEntity(CreateDocumentTypeDto createDto);
    
    void updateEntityFromDto(EditDocumentTypeDto editDto, @MappingTarget DocumentType entity);
}
