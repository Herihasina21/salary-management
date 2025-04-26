package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Payroll findByEmployeeId(Long employeeId);
}
