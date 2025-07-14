package com.java.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.backend.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);

	@Query("SELECT uc.user FROM UserCompanyEntity uc WHERE uc.company.id = :companyId")
    List<UserEntity> findUsersByCompanyId(@Param("companyId") Long companyId);
	
	boolean existsByUsername(String username);
	
//	List<UserEntity> findByCompanies_Id(Long companyId);
}
