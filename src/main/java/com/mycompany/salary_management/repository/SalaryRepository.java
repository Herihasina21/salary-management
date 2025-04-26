package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Salary findByEmployeeId(Long employeeId);
}
