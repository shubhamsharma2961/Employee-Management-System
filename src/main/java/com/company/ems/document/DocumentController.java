package com.company.ems.document;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentDto>> uploadDocument(
            @RequestParam("file") MultipartFile file, @RequestParam("documentTypeId") UUID documentTypeId) {
        DocumentDto result = documentService.createDocument(file, documentTypeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Uploaded", result));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('document:update')")
    public ResponseEntity<ApiResponse<DocumentDto>> updateDocument(@PathVariable UUID id, @RequestBody @Valid EditDocumentDto dto) {
        DocumentDto result = documentService.updateDocument(id, dto);
        ApiResponse<DocumentDto> response = new ApiResponse<>(true, "Document metadata updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('document:read')")
    public ResponseEntity<ApiResponse<DocumentDto>> getDocumentById(@PathVariable UUID id) {
        DocumentDto result = documentService.getDocumentById(id);
        ApiResponse<DocumentDto> response = new ApiResponse<>(true, "Document record fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('document:read')")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getAllActiveDocuments() {
        List<DocumentDto> result = documentService.getAllActiveDocuments();
        ApiResponse<List<DocumentDto>> response = new ApiResponse<>(true, "All active system documents fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('document:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Document soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
    
}