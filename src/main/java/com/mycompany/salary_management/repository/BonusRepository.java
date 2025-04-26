package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findByPayrollId(Long payrollId);
}
