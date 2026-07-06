package com.example.springbootjpa.service;

import com.example.springbootjpa.dto.EmployeeRequestDTO;
import com.example.springbootjpa.dto.EmployeeResponseDTO;
import com.example.springbootjpa.entity.Department;
import com.example.springbootjpa.entity.Employee;
import com.example.springbootjpa.exception.ResourceNotFoundException;
import com.example.springbootjpa.repository.DepartmentRepository;
import com.example.springbootjpa.repository.EmployeeRepository;
import com.example.springbootjpa.util.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        Employee employee = EmployeeMapper.toEntity(dto, department);

        employee.setCreatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(savedEmployee);

    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return EmployeeMapper.toDTO(employee);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {

        return employeeRepository.findAllWithDepartment()
                .stream()
                .map(EmployeeMapper::toDTO)
                .toList();
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                        .orElseThrow(() ->
                new ResourceNotFoundException(
                                "Department not found with ID: " + dto.getDepartmentId()));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());

        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with ID: " + id));

        employeeRepository.delete(employee);
    }
}