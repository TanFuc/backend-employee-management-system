package com.java.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.backend.dto.UserDTO;
import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.UserCompanyEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;
import com.java.backend.mapper.UserMapper;
import com.java.backend.repository.CompanyRepository;
import com.java.backend.repository.RoleRepository;
import com.java.backend.repository.UserCompanyRepository;
import com.java.backend.repository.UserRepository;
import com.java.backend.repository.UserRoleRepository;
import com.java.backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UserCompanyRepository userCompanyRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
	}

	@Override
	public UserDTO getUserById(Long id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
		return UserMapper.toDTO(user);
	}

	@Override
	public UserDTO createUser(UserDTO dto, Long companyId) {
		UserEntity user = new UserEntity();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setFullName(dto.getFullName());
		user.setActive(true);
		user = userRepository.save(user);

		for (String roleName : dto.getRoles()) {
			RoleEntity role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " + roleName));

			UserRoleEntity userRole = new UserRoleEntity();
			userRole.setUser(user);
			userRole.setRole(role);
			userRoleRepository.save(userRole);
		}

		CompanyEntity company = companyRepository.findById(companyId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy công ty"));

		UserCompanyEntity userCompany = new UserCompanyEntity();
		userCompany.setUser(user);
		userCompany.setCompany(company);
		userCompanyRepository.save(userCompany);

		return UserMapper.toDTO(user);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO dto) {
		UserEntity existing = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không hợp lệ"));

		existing.setUsername(dto.getUsername());

		userRepository.save(existing);

		return UserMapper.toDTO(existing);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public List<UserDTO> getUsersByCompany(Long companyId) {
		List<UserEntity> users = userRepository.findUsersByCompanyId(companyId);
		return users.stream().map(UserMapper::toDTO).toList();
	}
}
