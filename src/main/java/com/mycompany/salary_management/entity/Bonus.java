package com.mycompany.salary_management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Bonus {
    @Id @GeneratedValue
    private Long id;
    private String type;           // Ex: "Ancienneté", "Performance"
    private double amount;

    @ManyToMany(mappedBy = "bonuses")
    private List<Payroll> payrolls = new ArrayList<>();
}
