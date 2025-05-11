package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Optional<Payroll> findBySalaryId(Long salaryId);
}
