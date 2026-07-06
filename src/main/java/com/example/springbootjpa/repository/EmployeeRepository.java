package com.example.springbootjpa.repository;

import com.example.springbootjpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/// Repository Layer :- Responsible for Performing CRUD Ops
// Spring will automatically create implementation of this interface at runtime.
@Repository


// We Are Using Extends Because We're Implementing interface <-> interface
    public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /*
     *  <Employee, Long> -> Pass your Entity Class and Primary key Type as Parameter
     *
     *  JpaRepository already provides:
     *
     *  save()
     *  findById()
     *  findAll()
     *  deleteById()
     *  count()
     *  existsById()
     *  saveAll()
     *  deleteAll()
     *  pagination
     *  sorting
     *
     *  No implementation required.
     *
     */

    @Query("""
    SELECT e
    FROM Employee e
    JOIN FETCH e.department
    """)
    List<Employee> findAllWithDepartment();

}