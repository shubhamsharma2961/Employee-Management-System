package com.company.ems.documentType;

import com.company.ems.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document-types")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('document-type:create')")
    public ResponseEntity<ApiResponse<DocumentTypeDto>> create(@Valid @RequestBody CreateDocumentTypeDto createDto) {
        DocumentTypeDto data = documentTypeService.createDocumentType(createDto);
        ApiResponse<DocumentTypeDto> response = new ApiResponse<>(true, "Document type created successfully.", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('document-type:read')")
    public ResponseEntity<ApiResponse<List<DocumentTypeDto>>> getAllActive() {
        List<DocumentTypeDto> data = documentTypeService.getAllActiveDocumentTypes();
        ApiResponse<List<DocumentTypeDto>> response = new ApiResponse<>(true, "Active document types retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('document-type:read')")
    public ResponseEntity<ApiResponse<DocumentTypeDto>> getById(@PathVariable UUID id) {
        DocumentTypeDto data = documentTypeService.getDocumentTypeById(id);
        ApiResponse<DocumentTypeDto> response = new ApiResponse<>(true, "Document type retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('document-type:update')")
    public ResponseEntity<ApiResponse<DocumentTypeDto>> update(@PathVariable UUID id, @Valid @RequestBody EditDocumentTypeDto editDto) {
        DocumentTypeDto data = documentTypeService.updateDocumentType(id, editDto);
        ApiResponse<DocumentTypeDto> response = new ApiResponse<>(true, "Document type updated successfully.", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('document-type:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteSoft(@PathVariable UUID id) {
        documentTypeService.deleteDocumentType(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Document type deleted successfully.", null);
        return ResponseEntity.ok(response);
    }
}