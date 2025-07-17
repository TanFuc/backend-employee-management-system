package com.java.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.entity.UserEntity;
import com.java.backend.repository.UserRepository;
import com.java.backend.service.EmployeeService;
import com.java.backend.service.UserService;
import com.java.backend.dto.request.CurrentRequestContext;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CurrentRequestContext requestContext;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		Long companyId = requestContext.getCompanyId();
		List<EmployeeDTO> employees = employeeService.getAllEmployeesByCompany(companyId);
		return ResponseEntity.ok(employees);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or hasRole('STAFF')")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long employeeId,
			Authentication authentication) {
		Long companyId = requestContext.getCompanyId();
		String currentUsername = authentication.getName();

		EmployeeDTO employeeDTO = employeeService.getEmployeeByIdIfOwnerOrAdmin(employeeId, companyId, currentUsername);
		return ResponseEntity.ok(employeeDTO);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO,
			Authentication authentication) {
		Long companyId = requestContext.getCompanyId();
		EmployeeDTO created = employeeService.createEmployee(employeeDTO, companyId);

		UserEntity currentUser = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Người dùng không hợp lệ"));

		userService.addStaffRoleIfNeeded(currentUser, companyId);

		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
		Long companyId = requestContext.getCompanyId();
		employeeService.deleteEmployeeInCompany(employeeId, companyId);
		return ResponseEntity.ok("Xóa thành công!");
	}
	
	@PutMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
	public ResponseEntity<EmployeeDTO> updateEmployee(
	        @PathVariable("id") Long employeeId,
	        @RequestBody EmployeeDTO employeeDTO) {
	    Long companyId = requestContext.getCompanyId();
	    EmployeeDTO updatedEmployee = employeeService.updateEmployeeInCompany(employeeId, employeeDTO, companyId);
	    return ResponseEntity.ok(updatedEmployee);
	}
}
