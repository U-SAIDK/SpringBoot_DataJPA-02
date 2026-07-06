package com.example.springbootjpa.dto;

import lombok.Data;

/**
 * Request payload received from client
 * while creating/updating a Department.
 */

// When client will send Data in Json format ; jackson will convert json into object
//   {
//      "name":"Engineering",  ->  To DepartmentRequestDTO
//      "location":"Pune"
//   }
//


@Data
public class DepartmentRequestDTO {

    private String name;

    private String location;

}