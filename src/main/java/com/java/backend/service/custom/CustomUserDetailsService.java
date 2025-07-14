package com.java.backend.service.custom;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.backend.entity.UserEntity;
import com.java.backend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng!"));

		return new User(user.getUsername(), user.getPassword(),
				user.getUserRoles().stream()
						.map(userRole -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
								"ROLE_" + userRole.getRole().getName()))
						.collect(Collectors.toList()));
	}
}
