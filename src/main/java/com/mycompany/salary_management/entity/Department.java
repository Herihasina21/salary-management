package com.mycompany.salary_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_sequence")
    @SequenceGenerator(name = "department_sequence", sequenceName = "department_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String code;
}