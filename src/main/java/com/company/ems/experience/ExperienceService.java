package com.company.ems.experience;

import com.company.ems.common.ApprovalStatus;
import java.util.List;
import java.util.UUID;

public interface ExperienceService {
    ExperienceDto createExperience(CreateExperienceDto dto);
    ExperienceDto updateExperience(UUID id, EditExperienceDto dto);
    ExperienceDto getExperienceById(UUID id);
    List<ExperienceDto> getExperiencesByEmployeeId(UUID employeeId);
    List<ExperienceDto> getMyExperiences();
    List<ExperienceDto> getExperiencesByStatus(ApprovalStatus status);
    void updateStatus(UUID id, ApprovalStatus status, String remarks);
    void deleteExperience(UUID id);
}
