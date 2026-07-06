package com.example.springbootjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *     Used when sending data BACK to client
 */

@Data
@Builder // Because response objects are created from Entity: Entity → DTO mapping → response object Builder makes mapping clean.

public class EmployeeResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Double salary;

    private LocalDateTime createdAt;

    private DepartmentResponseDTO department;


}