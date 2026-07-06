package com.example.springbootjpa.dto;

import lombok.Data;

/**
 *   Used when client sends data TO backend
 */

@Data
public class EmployeeRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private Double salary;

    // Foreign Key
    private Long departmentId;

}