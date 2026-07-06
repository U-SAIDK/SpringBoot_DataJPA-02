package com.example.springbootjpa.util;

import com.example.springbootjpa.dto.DepartmentRequestDTO;
import com.example.springbootjpa.dto.DepartmentResponseDTO;
import com.example.springbootjpa.entity.Department;

public final class DepartmentMapper {

    private DepartmentMapper() {
    }

    /**
     * Request DTO -> Entity
     */
    public static Department toEntity(DepartmentRequestDTO dto) {

        return Department.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
    }

    /**
     * Entity -> Response DTO
     */
    public static DepartmentResponseDTO toDTO(Department department) {

        if (department == null) {
            return null;
        }

        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .location(department.getLocation())
                .build();
    }
}