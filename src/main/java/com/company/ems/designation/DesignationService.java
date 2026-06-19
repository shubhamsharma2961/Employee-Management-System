package com.company.ems.designation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.ems.security.jwt.UserPrincipal;

import java.util.UUID;

public interface DesignationService {
DesignationDto createDesignation(CreateDesignationDto dto);
    DesignationDto updateDesignation(UUID id, EditDesignationDto dto);
    Page<DesignationDto> getDesignations(String search, Pageable pageable);
    DesignationDto getDesignationById(UUID id);  
    void deleteDesignation(UUID id);
}