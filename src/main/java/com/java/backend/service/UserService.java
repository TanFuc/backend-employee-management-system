package com.java.backend.service;

import java.util.List;

import com.java.backend.dto.UserDTO;
import com.java.backend.entity.UserEntity;

public interface UserService {
	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long id);

	UserDTO createUser(UserDTO dto, Long companyId);

	UserDTO updateUser(Long id, UserDTO dto);

	void deleteUser(Long id);
	
	List<UserDTO> getUsersByCompany(Long companyId);
	
	void addStaffRoleIfNeeded(UserEntity user, Long companyId);
}
