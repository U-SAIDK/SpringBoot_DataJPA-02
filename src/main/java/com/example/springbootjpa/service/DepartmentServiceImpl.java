package com.example.springbootjpa.service;

import com.example.springbootjpa.dto.DepartmentRequestDTO;
import com.example.springbootjpa.dto.DepartmentResponseDTO;
import com.example.springbootjpa.entity.Department;
import com.example.springbootjpa.exception.ResourceNotFoundException;
import com.example.springbootjpa.repository.DepartmentRepository;
import com.example.springbootjpa.util.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * Create Department
     */
    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {

        Department department = DepartmentMapper.toEntity(dto);

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toDTO(savedDepartment);
    }

    /**
     * Get Department by ID
     */
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with ID: " + id));

        return DepartmentMapper.toDTO(department);
    }

    /**
     * Get All Departments
     */
    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .toList();
    }

    /**
     * Update Department
     */
    @Override
    public DepartmentResponseDTO updateDepartment(Long id,
                                                  DepartmentRequestDTO dto) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with ID: " + id));

        department.setName(dto.getName());
        department.setLocation(dto.getLocation());

        Department updatedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toDTO(updatedDepartment);
    }

    /**
     * Delete Department
     */
    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with ID: " + id));

        departmentRepository.delete(department);
    }
}