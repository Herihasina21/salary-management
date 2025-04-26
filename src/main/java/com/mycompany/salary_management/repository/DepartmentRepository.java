package com.mycompany.salary_management.repository;

import com.mycompany.salary_management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository  extends JpaRepository<Department, Long> {
}
