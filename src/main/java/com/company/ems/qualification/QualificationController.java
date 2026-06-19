package com.company.ems.qualification;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.common.ApprovalStatus;
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/qualifications")
public class QualificationController {

    private final QualificationService qualificationService;

    public QualificationController(QualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('qualification:create')")
    public ResponseEntity<ApiResponse<QualificationDto>> createQualification(@RequestBody @Valid CreateQualificationDto dto) {
        QualificationDto result = qualificationService.createQualification(dto);
        ApiResponse<QualificationDto> response = new ApiResponse<>(true, "Qualification submitted successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification:update')")
    public ResponseEntity<ApiResponse<QualificationDto>> updateQualification(@PathVariable UUID id, @RequestBody @Valid EditQualificationDto dto) {
        QualificationDto result = qualificationService.updateQualification(id, dto);
        ApiResponse<QualificationDto> response = new ApiResponse<>(true, "Qualification updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification:read')")
    public ResponseEntity<ApiResponse<QualificationDto>> getQualificationById(@PathVariable UUID id) {
        QualificationDto result = qualificationService.getQualificationById(id);
        ApiResponse<QualificationDto> response = new ApiResponse<>(true, "Qualification fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('qualification:read_own')")
    public ResponseEntity<ApiResponse<List<QualificationDto>>> getMyQualifications() {
        List<QualificationDto> result = qualificationService.getMyQualifications();
        return ResponseEntity.ok(new ApiResponse<>(true, "Your qualifications loaded successfully", result));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('qualification:read_all')")
    public ResponseEntity<ApiResponse<List<QualificationDto>>> getQualificationsByEmployeeId(@PathVariable UUID employeeId) {
        List<QualificationDto> result = qualificationService.getQualificationsByEmployeeId(employeeId);
        ApiResponse<List<QualificationDto>> response = new ApiResponse<>(true, "Employee qualifications loaded successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAuthority('qualification:review')")
    public ResponseEntity<ApiResponse<List<QualificationDto>>> getQualificationsByStatus(@RequestParam ApprovalStatus status) {
        List<QualificationDto> result = qualificationService.getQualificationsByStatus(status);
        ApiResponse<List<QualificationDto>> response = new ApiResponse<>(true, "Qualifications queue loaded successfully", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('qualification:verify')")
    public ResponseEntity<ApiResponse<Void>> verifyQualification(
            @PathVariable UUID id,
            @RequestParam ApprovalStatus status,
            @RequestParam(required = false) String remarks) {
        qualificationService.updateStatus(id, status, remarks);
        ApiResponse<Void> response = new ApiResponse<>(true, "Verification decision updated successfully", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('qualification:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteQualification(@PathVariable UUID id) {
        qualificationService.deleteQualification(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Qualification soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
