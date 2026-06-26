package com.example.springbootjpa.service;

import com.example.springbootjpa.entity.Employee;
import com.example.springbootjpa.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor    // Lombok Library Automates ArgsConstructor Making
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee){
        employee.setCreatedAt(LocalDateTime.now());
       return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(()-> new RuntimeException("Employee not found"));

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setDepartment(employee.getDepartment());

        return employeeRepository.save(existingEmployee);

    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }


}
