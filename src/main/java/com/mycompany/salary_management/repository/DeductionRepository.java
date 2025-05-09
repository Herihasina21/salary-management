package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    @Query("SELECT d FROM Deduction d JOIN d.payrolls p WHERE p.id = :payrollId")
    List<Deduction> findByPayrollId(@Param("payrollId") Long payrollId);
}
