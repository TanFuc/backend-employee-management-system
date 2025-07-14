package com.java.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.java.backend.dto.UserDTO;
import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;
import com.java.backend.mapper.UserMapper;
import com.java.backend.repository.UserRepository;
import com.java.backend.service.UserService;
import com.java.backend.utils.RoleUtils;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private com.java.backend.dto.request.CurrentRequestContext requestContext;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		Long companyId = requestContext.getCompanyId();
		return ResponseEntity.ok(userService.getUsersByCompany(companyId));
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id, Authentication authentication) {
		UserEntity currentUser = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Người dùng không hợp lệ!"));

		UserEntity targetUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

		int currentLevel = currentUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);
		int targetLevel = targetUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);

		if (!RoleUtils.hasPermission(currentLevel, targetLevel)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok(UserMapper.toDTO(targetUser));
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
		Long companyId = requestContext.getCompanyId();
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto, companyId));
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto,
			Authentication authentication) {
		UserEntity currentUser = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Người dùng không hợp lệ!"));

		UserEntity targetUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

		int currentLevel = currentUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);
		int targetLevel = targetUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);

		if (!RoleUtils.hasPermission(currentLevel, targetLevel)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		return ResponseEntity.ok(userService.updateUser(id, dto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
		UserEntity currentUser = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Người dùng không hợp lệ!"));

		UserEntity targetUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

		int currentLevel = currentUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);
		int targetLevel = targetUser.getUserRoles().stream()
				.mapToInt(userRole -> userRole.getRole().getLevel()).max().orElse(0);

		if (!RoleUtils.hasPermission(currentLevel, targetLevel)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
