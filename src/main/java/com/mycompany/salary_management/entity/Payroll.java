package com.mycompany.salary_management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class Payroll {
    @Id @GeneratedValue
    private Long id;
    private LocalDate periodStart;  // Début de la période
    private LocalDate periodEnd;    // Fin de la période
    private double netSalary;       // Salaire net

    @OneToMany(mappedBy = "payroll")
    private List<Bonus> bonuses;

    @OneToMany(mappedBy = "payroll")
    private List<Deduction> deductions;

    @ManyToOne
    private Employee employee;
}
