package com.mycompany.salary_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;
    private String name;           // Ex: "RH", "Comptabilité"
    private String code;           // Ex: "HR", "ACC"
}
