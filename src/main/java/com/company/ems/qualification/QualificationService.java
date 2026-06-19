package com.company.ems.qualification;

import java.util.List;
import java.util.UUID;

import com.company.ems.common.ApprovalStatus;

public interface QualificationService {
    QualificationDto createQualification(CreateQualificationDto dto);
    QualificationDto updateQualification(UUID id, EditQualificationDto dto);
    QualificationDto getQualificationById(UUID id);
    List<QualificationDto> getQualificationsByEmployeeId(UUID employeeId);
    List<QualificationDto> getMyQualifications();
    List<QualificationDto> getQualificationsByStatus(ApprovalStatus status);
    void updateStatus(UUID id, ApprovalStatus status, String remarks);
    void deleteQualification(UUID id);
}
