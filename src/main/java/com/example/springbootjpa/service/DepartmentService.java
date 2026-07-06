package com.example.springbootjpa.service;

import com.example.springbootjpa.dto.DepartmentRequestDTO;
import com.example.springbootjpa.dto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto);

    DepartmentResponseDTO getDepartmentById(Long id);

    List<DepartmentResponseDTO> getAllDepartments();

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto);

    void deleteDepartment(Long id);

}