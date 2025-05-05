package com.mycompany.salary_management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class PayrollDTO {
    private Long id;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double grossSalary;
    private double netSalary;

    private List<BonusDTO> bonuses;
    private List<DeductionDTO> deductions;

    private Long employeeId;
    private String employeeName; // or fullName, depending on your model
}

