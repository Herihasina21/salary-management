package com.mycompany.salary_management.dto;

import lombok.Data;

@Data
public class SalaryDTO {
    Long id;
    double baseSalary;
    Long employeeId;
}
