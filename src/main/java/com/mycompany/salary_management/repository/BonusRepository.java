package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Long> {
    @Query("SELECT b FROM Bonus b JOIN b.payrolls p WHERE p.id = :payrollId")
    List<Bonus> findByPayrollId(@Param("payrollId") Long payrollId);
}
