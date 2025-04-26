package com.mycompany.salary_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String firstName;
    private String email;
    private String phone;
    private String address;
    private String position;       // Poste (ex: "Développeur")
    private LocalDate hireDate;     // Date d'embauche
    private String contractType;    // CDI, CDD, etc.

    @ManyToOne
    private Department department; // Service/Département
}
