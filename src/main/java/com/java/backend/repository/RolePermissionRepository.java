package com.java.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.RoleEntity;
import com.java.backend.entity.RolePermissionEntity;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
	List<RolePermissionEntity> findAllByRoleIn(List<RoleEntity> roles);
}
