package com.java.backend.service.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.backend.entity.PermissionEntity;
import com.java.backend.entity.RolePermissionEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;
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

		Set<GrantedAuthority> authorities = new HashSet<>();

		for (UserRoleEntity userRole : user.getUserRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()));

			List<RolePermissionEntity> rolePermissions = userRole.getRole().getRolePermissions();
			if (rolePermissions != null) {
				for (RolePermissionEntity rp : rolePermissions) {
					String permissionName = rp.getPermission().getName();
					authorities.add(new SimpleGrantedAuthority(permissionName));
				}
			}
		}

		return new User(user.getUsername(), user.getPassword(), authorities);
	}

}
