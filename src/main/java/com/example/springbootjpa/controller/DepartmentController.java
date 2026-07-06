package com.example.springbootjpa.controller;

import com.example.springbootjpa.dto.DepartmentRequestDTO;
import com.example.springbootjpa.dto.DepartmentResponseDTO;
import com.example.springbootjpa.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * CREATE Department
     */
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO dto) {

        DepartmentResponseDTO response = departmentService.createDepartment(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET Department By ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {

        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    /**
     * GET All Departments
     */
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {

        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    /**
     * UPDATE Department
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestDTO dto)
    {

        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    /**
     * DELETE Department
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok("Department deleted successfully.");
    }

}