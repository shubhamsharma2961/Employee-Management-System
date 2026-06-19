package com.company.ems.document;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


public interface DocumentService {
    DocumentDto getDocumentById(UUID id);
    List<DocumentDto> getAllActiveDocuments();
    DocumentDto createDocument (MultipartFile file, UUID documentTypeId);
    DocumentDto updateDocument(UUID id, EditDocumentDto dto);
    void deleteDocument(UUID id);
}