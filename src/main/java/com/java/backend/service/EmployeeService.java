package com.java.backend.service;

import java.util.List;

import com.java.backend.dto.EmployeeDTO;

public interface EmployeeService {
	EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
	
	EmployeeDTO getEmployeeById(Long employeeId);
	
	List<EmployeeDTO> getAllEmployees();
	
	EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee);

	void deleteEmployee(Long employeeId);
}
