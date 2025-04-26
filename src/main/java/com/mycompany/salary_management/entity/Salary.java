package com.mycompany.salary_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Salary {
    @Id @GeneratedValue
    private Long id;
    private double baseSalary;      // Salaire de base

    @OneToOne
    private Employee employee;
}
