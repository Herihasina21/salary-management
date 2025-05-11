package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BonusRepository extends JpaRepository<Bonus, Long> {
}
