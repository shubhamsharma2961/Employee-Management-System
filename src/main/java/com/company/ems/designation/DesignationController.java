package com.company.ems.designation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('designation:create')")
    public ResponseEntity<ApiResponse<DesignationDto>> createDesignation(@RequestBody @Valid CreateDesignationDto dto) {
        DesignationDto result = designationService.createDesignation(dto);
        ApiResponse<DesignationDto> response = new ApiResponse<>(true, "Designation created successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('designation:update')")
    public ResponseEntity<ApiResponse<DesignationDto>> updateDesignation(@PathVariable UUID id, @RequestBody @Valid EditDesignationDto dto) {
        DesignationDto result = designationService.updateDesignation(id, dto);
        ApiResponse<DesignationDto> response = new ApiResponse<>(true, "Designation updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('designation:read')")
    public ResponseEntity<ApiResponse<DesignationDto>> getDesignationById(@PathVariable UUID id) {
        DesignationDto result = designationService.getDesignationById(id);
        ApiResponse<DesignationDto> response = new ApiResponse<>(true, "Designation details fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('designation:read', 'designation:lookup')")
    public ResponseEntity<ApiResponse<Page<DesignationDto>>> getAllDesignations(@RequestParam(value = "search", required = false) String search,@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<DesignationDto> result = designationService.getDesignations(search, pageable);
        ApiResponse<Page<DesignationDto>> response = new ApiResponse<>(true, "System designations fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('designation:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteDesignation(@PathVariable UUID id) {
        designationService.deleteDesignation(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Designation soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}