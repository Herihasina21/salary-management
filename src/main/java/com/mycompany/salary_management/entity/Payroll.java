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
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    @ManyToOne
    private Employee employee;

    @OneToOne
    private Salary salary;

    @OneToMany(mappedBy = "payroll")
    private List<Bonus> bonuses;

    @OneToMany(mappedBy = "payroll")
    private List<Deduction> deductions;

    @Transient
    public double getGrossSalary() {
        double base = salary != null ? salary.getBaseSalary() : 0.0;
        double bonus = bonuses != null ? bonuses.stream().mapToDouble(Bonus::getAmount).sum() : 0.0;
        return base + bonus;
    }

    @Transient
    public double getNetSalary() {
        double gross = getGrossSalary();
        double deduction = deductions != null ? deductions.stream().mapToDouble(Deduction::getAmount).sum() : 0.0;
        return gross - deduction;
    }
}
