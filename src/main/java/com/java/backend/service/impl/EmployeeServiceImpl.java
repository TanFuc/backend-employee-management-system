package com.java.backend.service.impl;

import com.java.backend.dto.EmployeeDTO;
import com.java.backend.entity.CompanyEntity;
import com.java.backend.entity.EmployeeEntity;
import com.java.backend.entity.UserEntity;
import com.java.backend.exception.ResourceNotFoundException;
import com.java.backend.mapper.EmployeeMapper;
import com.java.backend.repository.CompanyRepository;
import com.java.backend.repository.EmployeeRepository;
import com.java.backend.repository.UserRepository;
import com.java.backend.service.EmployeeService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private boolean isAdminOrManager(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUserRoles().stream()
                .anyMatch(ur -> {
                    String roleName = ur.getRole().getName();
                    return roleName.equalsIgnoreCase("ADMIN") || roleName.equalsIgnoreCase("MANAGER");
                });
    }

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công ty"));

        EmployeeEntity employee = EmployeeMapper.toEntity(employeeDTO);
        employee.setCompany(company);
        EmployeeEntity saved = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(saved);
    }

    @Override
    public EmployeeDTO getEmployeeByIdInCompany(Long employeeId, Long companyId) {
        EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại hoặc không thuộc công ty"));
        return EmployeeMapper.toDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesByCompany(Long companyId) {
        List<EmployeeEntity> employees = employeeRepository.findAllByCompany_Id(companyId);
        return employees.stream().map(EmployeeMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployeeInCompany(Long employeeId, EmployeeDTO updatedEmployee, Long companyId) {
        EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại hoặc không thuộc công ty"));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPhone(updatedEmployee.getPhone());
        employee.setGender(updatedEmployee.getGender());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setPosition(updatedEmployee.getPosition());
        employee.setStatus(updatedEmployee.getStatus());
        employee.setDob(updatedEmployee.getDob());
        employee.setAddress(updatedEmployee.getAddress());

        EmployeeEntity updated = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(updated);
    }

    @Override
    public void deleteEmployeeInCompany(Long employeeId, Long companyId) {
        EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại hoặc không thuộc công ty"));
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDTO updateEmployeeIfOwnerOrAdmin(Long employeeId, EmployeeDTO dto, Long companyId,
            String currentUsername) {
        EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên trong công ty"));

        UserEntity employeeOwner = employee.getUser();
        boolean isOwner = employeeOwner != null && employeeOwner.getUsername().equals(currentUsername);

        if (!isOwner) {
            throw new RuntimeException("Bạn không có quyền cập nhật nhân viên này");
        }

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setGender(dto.getGender());
        employee.setDepartment(dto.getDepartment());
        employee.setPosition(dto.getPosition());
        employee.setStatus(dto.getStatus());
        employee.setDob(dto.getDob());

        EmployeeEntity updated = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(updated);
    }

    @Override
    public EmployeeDTO getEmployeeByIdIfOwnerOrAdmin(Long employeeId, Long companyId, String currentUsername) {
        EmployeeEntity employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        if (!isAdminOrManager(currentUsername)) {
            if (employee.getUser() == null || !employee.getUser().getUsername().equals(currentUsername)) {
                throw new RuntimeException("Bạn không có quyền truy cập thông tin nhân viên này");
            }
        }

        return EmployeeMapper.toDTO(employee);
    }
}
