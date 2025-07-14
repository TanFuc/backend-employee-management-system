package com.java.backend.mapper;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.entity.EmployeeEntity;

public class EmployeeMapper {

	public static EmployeeDTO toDTO(EmployeeEntity entity) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setAddress(entity.getAddress());
		dto.setGender(entity.getGender());
		dto.setDepartment(entity.getDepartment());
		dto.setPosition(entity.getPosition());
		dto.setStatus(entity.getStatus());
		dto.setDob(entity.getDob());
		return dto;
	}

	public static EmployeeEntity toEntity(EmployeeDTO dto) {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setId(dto.getId());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setAddress(dto.getAddress());
		entity.setGender(dto.getGender());
		entity.setDepartment(dto.getDepartment());
		entity.setPosition(dto.getPosition());
		entity.setStatus(dto.getStatus());
		entity.setDob(dto.getDob());
		return entity;
	}
}
