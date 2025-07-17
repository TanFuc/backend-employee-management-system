
package com.java.backend.service;

import com.java.backend.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getRolesByCompanyId(Long companyId);

}