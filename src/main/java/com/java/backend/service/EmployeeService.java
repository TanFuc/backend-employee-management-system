package com.java.backend.service;

import java.util.List;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.entity.UserEntity;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO, Long companyId);

    EmployeeDTO getEmployeeByIdInCompany(Long employeeId, Long companyId);

    List<EmployeeDTO> getAllEmployeesByCompany(Long companyId);

    EmployeeDTO updateEmployeeInCompany(Long employeeId, EmployeeDTO updatedEmployee, Long companyId);

    void deleteEmployeeInCompany(Long employeeId, Long companyId);
    
    EmployeeDTO updateEmployeeIfOwnerOrAdmin(Long employeeId, EmployeeDTO dto, Long companyId, String currentUsername);

    EmployeeDTO getEmployeeByIdIfOwnerOrAdmin(Long employeeId, Long companyId, String currentUsername);
}
