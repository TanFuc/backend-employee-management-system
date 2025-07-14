package com.java.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		List<EmployeeDTO> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

	@PostMapping
	public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
		return new ResponseEntity<>(employeeService.createEmployee(employeeDTO), HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long employeeId) {
		EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
		return ResponseEntity.ok(employeeDTO);
	}

	@PutMapping("{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId,
			@RequestBody EmployeeDTO updateEmployee) {
		EmployeeDTO employeeDTO = employeeService.updateEmployee(employeeId, updateEmployee);
		return ResponseEntity.ok(employeeDTO);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
		System.out.println("DELETE /employee/" + employeeId);
		employeeService.deleteEmployee(employeeId);
		return ResponseEntity.ok("Xóa thành công!");
	}
}
