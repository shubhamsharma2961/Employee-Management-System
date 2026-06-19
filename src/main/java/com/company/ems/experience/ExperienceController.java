package com.company.ems.experience;

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
@RequestMapping("/api/v1/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('experience:create')")
    public ResponseEntity<ApiResponse<ExperienceDto>> createExperience(@RequestBody @Valid CreateExperienceDto dto) {
        ExperienceDto result = experienceService.createExperience(dto);
        ApiResponse<ExperienceDto> response = new ApiResponse<>(true, "Work experience submitted successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('experience:update')")
    public ResponseEntity<ApiResponse<ExperienceDto>> updateExperience(@PathVariable UUID id, @RequestBody @Valid EditExperienceDto dto) {
        ExperienceDto result = experienceService.updateExperience(id, dto);
        ApiResponse<ExperienceDto> response = new ApiResponse<>(true, "Work experience updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('experience:read')")
    public ResponseEntity<ApiResponse<ExperienceDto>> getExperienceById(@PathVariable UUID id) {
        ExperienceDto result = experienceService.getExperienceById(id);
        ApiResponse<ExperienceDto> response = new ApiResponse<>(true, "Work experience fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('experience:read_own')")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getMyExperiences() {
        List<ExperienceDto> result = experienceService.getMyExperiences();
        return ResponseEntity.ok(new ApiResponse<>(true, "Your work experiences loaded successfully", result));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('experience:read_all')")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getExperiencesByEmployeeId(@PathVariable UUID employeeId) {
        List<ExperienceDto> result = experienceService.getExperiencesByEmployeeId(employeeId);
        ApiResponse<List<ExperienceDto>> response = new ApiResponse<>(true, "Employee work experiences loaded successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAuthority('experience:review')")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getExperiencesByStatus(@RequestParam ApprovalStatus status) {
        List<ExperienceDto> result = experienceService.getExperiencesByStatus(status);
        ApiResponse<List<ExperienceDto>> response = new ApiResponse<>(true, "Experiences approval queue loaded successfully", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('experience:verify')")
    public ResponseEntity<ApiResponse<Void>> verifyExperience(
            @PathVariable UUID id,
            @RequestParam ApprovalStatus status,
            @RequestParam(required = false) String remarks) {
        experienceService.updateStatus(id, status, remarks);
        ApiResponse<Void> response = new ApiResponse<>(true, "Verification decision updated successfully", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('experience:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable UUID id) {
        experienceService.deleteExperience(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Work experience soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}