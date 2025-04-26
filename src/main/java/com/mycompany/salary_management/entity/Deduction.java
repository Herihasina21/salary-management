package com.mycompany.salary_management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Deduction {
    @Id @GeneratedValue
    private Long id;
    private String type;           // Ex: "CNAPS", "OSTIE", "Absence"
    private double amount;

    @ManyToOne
    private Payroll payroll;
}
