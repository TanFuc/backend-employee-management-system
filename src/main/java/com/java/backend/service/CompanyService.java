package com.java.backend.service;

import com.java.backend.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getAllCompanies();
    CompanyDTO getCompanyById(Long id);
    CompanyDTO createCompany(CompanyDTO dto);
    CompanyDTO updateCompany(Long id, CompanyDTO dto);
    void deleteCompany(Long id);
}
