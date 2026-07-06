package com.example.springbootjpa.util;

import com.example.springbootjpa.dto.EmployeeRequestDTO;
import com.example.springbootjpa.dto.EmployeeResponseDTO;
import com.example.springbootjpa.entity.Department;
import com.example.springbootjpa.entity.Employee;

public final class EmployeeMapper {

    private EmployeeMapper() {
    }

    /**
     * Convert Request DTO -> Entity
     */
    public static Employee toEntity(EmployeeRequestDTO dto,
                                    Department department) {

        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .salary(dto.getSalary())
                .department(department)
                .build();
    }

    /**
     * Convert Entity -> Response DTO
     */
    public static EmployeeResponseDTO toDTO(Employee employee) {

        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .salary(employee.getSalary())
                .createdAt(employee.getCreatedAt())
                .department(
                        DepartmentMapper.toDTO(employee.getDepartment())
                )
                .build();
    }
}