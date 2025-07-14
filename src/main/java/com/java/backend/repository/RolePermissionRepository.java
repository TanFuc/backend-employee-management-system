package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.RolePermissionEntity;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {}
