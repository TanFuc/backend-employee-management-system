package com.java.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.backend.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {}
