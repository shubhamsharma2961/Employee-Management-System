package com.company.ems.qualificationType;

import java.util.List;
import java.util.UUID;

public interface QualificationTypeService {
    QualificationTypeDto createQualificationType(CreateQualificationTypeDto createDto);
    List<QualificationTypeDto> getAllActiveQualificationTypes();
    QualificationTypeDto getQualificationTypeById(UUID id);
    QualificationTypeDto updateQualificationType(UUID id, EditQualificationTypeDto editDto);
    void deleteQualificationType(UUID id);
}