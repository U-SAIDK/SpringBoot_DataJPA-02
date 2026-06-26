package com.example.springbootjpa.repository;

import com.example.springbootjpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/// Repository Layer :- Responsible for Performing CRUD Ops
// Spring will automatically create implementation of this interface at runtime.
@Repository


// we are using extends because we're implementing interface <-> interface
    public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /*
     * <Employee, Long> -> Pass your Entity Class and Primary key Type as Parameter
     *
     *  JpaRepository already provides:
     *
     * save()
     * findById()
     * findAll()
     * deleteById()
     * count()
     * existsById()
     * saveAll()
     * deleteAll()
     * pagination
     * sorting
     *
     * No implementation required.
     */
}