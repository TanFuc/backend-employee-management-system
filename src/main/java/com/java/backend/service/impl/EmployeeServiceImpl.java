package com.java.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.entity.EmployeeEntity;
import com.java.backend.exception.ResourceNotFoundException;
import com.java.backend.mapper.EmployeeMapper;
import com.java.backend.repository.EmployeeRepository;
import com.java.backend.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
		EmployeeEntity employee = EmployeeMapper.toEntity(employeeDTO);
		EmployeeEntity savedEmployee = employeeRepository.save(employee);
		return EmployeeMapper.toDTO(savedEmployee);
	}

	@Override
	public EmployeeDTO getEmployeeById(Long employeeId) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Nhân viên " + employeeId + " không tồn tại!"));
		return EmployeeMapper.toDTO(employee);
	}

	@Override
	public List<EmployeeDTO> getAllEmployees() {
		List<EmployeeEntity> employees = employeeRepository.findAll();
		return employees.stream()
				.map(EmployeeMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployee) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại!"));

		employee.setFirstName(updatedEmployee.getFirstName());
		employee.setLastName(updatedEmployee.getLastName());
		employee.setEmail(updatedEmployee.getEmail());
		employee.setPhone(updatedEmployee.getPhone());
		employee.setGender(updatedEmployee.getGender());
		employee.setDepartment(updatedEmployee.getDepartment());
		employee.setPosition(updatedEmployee.getPosition());
		employee.setStatus(updatedEmployee.getStatus());
		employee.setDob(updatedEmployee.getDob());
		// employee.setAddress(updatedEmployee.getAddress());

		EmployeeEntity updatedEmployeeObj = employeeRepository.save(employee);
		return EmployeeMapper.toDTO(updatedEmployeeObj);
	}

	@Override
	public void deleteEmployee(Long employeeId) {
		employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại!"));

		employeeRepository.deleteById(employeeId);
	}
}
