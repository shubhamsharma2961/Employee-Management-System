package com.company.ems.qualificationType;

import com.company.ems.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qualification-types")
public class QualificationTypeController {

    private final QualificationTypeService qualificationTypeService;

    public QualificationTypeController(QualificationTypeService qualificationTypeService) {
        this.qualificationTypeService = qualificationTypeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('qualification-type:create')")
    public ResponseEntity<ApiResponse<QualificationTypeDto>> create(@Valid @RequestBody CreateQualificationTypeDto createDto) {
        QualificationTypeDto data = qualificationTypeService.createQualificationType(createDto);
        ApiResponse<QualificationTypeDto> response = new ApiResponse<>(true, "Qualification type created successfully.", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('qualification-type:read', 'qualification-type:lookup')")
    public ResponseEntity<ApiResponse<List<QualificationTypeDto>>> getAllActive() {
        List<QualificationTypeDto> data = qualificationTypeService.getAllActiveQualificationTypes();
        ApiResponse<List<QualificationTypeDto>> response = new ApiResponse<>(true, "Active qualification types retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification-type:read')")
    public ResponseEntity<ApiResponse<QualificationTypeDto>> getById(@PathVariable UUID id) {
        QualificationTypeDto data = qualificationTypeService.getQualificationTypeById(id);
        ApiResponse<QualificationTypeDto> response = new ApiResponse<>(true, "Qualification type retrieved successfully.", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification-type:update')")
    public ResponseEntity<ApiResponse<QualificationTypeDto>> update(@PathVariable UUID id, @Valid @RequestBody EditQualificationTypeDto editDto) {
        QualificationTypeDto data = qualificationTypeService.updateQualificationType(id, editDto);
        ApiResponse<QualificationTypeDto> response = new ApiResponse<>(true, "Qualification type updated successfully.", data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification-type:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteSoft(@PathVariable UUID id) {
        qualificationTypeService.deleteQualificationType(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Qualification type deleted successfully.", null);
        return ResponseEntity.ok(response);
    }
}