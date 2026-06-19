package com.company.ems.company;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RestController;

import com.company.ems.util.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('company:create')")
    public ResponseEntity<ApiResponse<CompanyDto>> createCompany(@RequestBody @Valid CreateCompanyDto dto) {
        CompanyDto result = companyService.createCompany(dto);
        ApiResponse<CompanyDto> response = new ApiResponse<>(true, "Company profile initialized successfully", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('company:update')")
    public ResponseEntity<ApiResponse<CompanyDto>> updateCompany(@PathVariable UUID id, @RequestBody @Valid EditCompanyDto dto) {
        CompanyDto result = companyService.updateCompany(id, dto);
        ApiResponse<CompanyDto> response = new ApiResponse<>(true, "Company profile updated successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('company:read')")
    public ResponseEntity<ApiResponse<CompanyDto>> getCompanyById(@PathVariable UUID id) {
        CompanyDto result = companyService.getCompanyById(id);
        ApiResponse<CompanyDto> response = new ApiResponse<>(true, "Company profile fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('company:read')")
    public ResponseEntity<ApiResponse<List<CompanyDto>>> getAllCompanyRecords() {
        List<CompanyDto> result = companyService.getAllCompanyRecords();
        ApiResponse<List<CompanyDto>> response = new ApiResponse<>(true, "All active company records fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('company:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Company profile soft-deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}