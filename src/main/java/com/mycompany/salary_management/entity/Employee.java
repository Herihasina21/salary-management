package com.mycompany.salary_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String firstName;
    private String email;
    private String phone;
    private String address;
    private String position;
    private LocalDate hireDate;
    private String contractType;

    @ManyToOne
    private Department department;
}
