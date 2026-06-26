package com.example.springbootjpa.service;

import com.example.springbootjpa.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}