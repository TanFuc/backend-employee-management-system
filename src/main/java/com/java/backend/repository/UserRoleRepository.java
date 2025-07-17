package com.java.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.backend.entity.UserEntity;
import com.java.backend.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
	@Query("SELECT ur FROM UserRoleEntity ur WHERE ur.user = :user AND ur.role.company.id = :companyId")
    List<UserRoleEntity> findAllByUserAndCompanyId(@Param("user") UserEntity user, @Param("companyId") Long companyId);
}
