package com.company.ems.documentType;

import java.util.List;
import java.util.UUID;

public interface DocumentTypeService {
    DocumentTypeDto createDocumentType(CreateDocumentTypeDto createDto);
    List<DocumentTypeDto> getAllActiveDocumentTypes();
    DocumentTypeDto getDocumentTypeById(UUID id);
    DocumentTypeDto updateDocumentType(UUID id, EditDocumentTypeDto editDto);
    void deleteDocumentType(UUID id);
}