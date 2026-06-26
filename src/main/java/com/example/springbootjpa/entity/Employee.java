package com.example.springbootjpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/// Marks class as a JPA-managed entity.
@Entity // Marks These as Entity Class which Hibernate will map to database
@Table(name = "employees") // Name of the Table to Create
@Getter                    // Lombok Library Automatically Generates Getter
@Setter                    // Setters & Constructors(args & no args)
@NoArgsConstructor
@AllArgsConstructor
@Builder                 //
public class Employee {

    @Id  // Marks these as primary key(sql)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Generate & increment Values
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100) // length = Sql(Varchar = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false) // unique :- NO SAME VALUE ALLOWED
    private String email;

    private Double salary;

    private String department;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

//  Equivalent Sql Code :-
// CREATE TABLE employees(
//    id BIGSERIAL PRIMARY KEY,
//
//    first_name VARCHAR(100) NOT NULL,
//
//    last_name VARCHAR(100) NOT NULL,
//
//    email VARCHAR(255) UNIQUE NOT NULL,
//
//    salary DOUBLE PRECISION,
//
//    department VARCHAR(255),
//
//    created_at TIMESTAMP
//    );