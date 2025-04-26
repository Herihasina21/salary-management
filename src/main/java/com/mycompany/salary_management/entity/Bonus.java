package com.mycompany.salary_management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Bonus {
    @Id @GeneratedValue
    private Long id;
    private String type;           // Ex: "Ancienneté", "Performance"
    private double amount;

    @ManyToOne
    private Payroll payroll;       // Lien vers la fiche de paie
}
