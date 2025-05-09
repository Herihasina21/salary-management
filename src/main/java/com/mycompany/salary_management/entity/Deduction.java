package com.mycompany.salary_management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Deduction {
    @Id @GeneratedValue
    private Long id;
    private String type;           // Ex: "CNAPS", "OSTIE", "Absence"
    private double amount;

    @ManyToMany(mappedBy = "deductions")
    private List<Payroll> payrolls = new ArrayList<>();
}
