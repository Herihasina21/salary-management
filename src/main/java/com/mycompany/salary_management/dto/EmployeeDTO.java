package com.mycompany.salary_management.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDTO {
    Long id;
    String name;
    String firstName;
    String email;
    String phone;
    String address;
    String position;
    LocalDate hireDate;
    String contractType;
    Long departmentID;
}
