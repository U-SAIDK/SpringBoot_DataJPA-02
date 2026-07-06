package com.example.springbootjpa.service;

import com.example.springbootjpa.dto.EmployeeRequestDTO;
import com.example.springbootjpa.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);

    EmployeeResponseDTO getEmployeeById(Long id);

    List<EmployeeResponseDTO> getAllEmployees();

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO);

    void deleteEmployee(Long id);
}