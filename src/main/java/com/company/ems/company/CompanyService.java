package com.company.ems.company;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
	CompanyDto getCompanyById(UUID id);
    List<CompanyDto> getAllCompanyRecords();
    CompanyDto createCompany(CreateCompanyDto createCompanyDto);
    CompanyDto updateCompany(UUID id, EditCompanyDto editCompanyDto);
    void deleteCompany(UUID id);
}
