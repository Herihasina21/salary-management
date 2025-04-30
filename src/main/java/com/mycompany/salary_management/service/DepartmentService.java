package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Department;
import com.mycompany.salary_management.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeService employeeService; // Injection de EmployeeService

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Département non trouvé avec l'ID : " + id));
    }

    public Department updateDepartment(Long id, Department department) {
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    department.setId(id);
                    return departmentRepository.save(department);
                })
                .orElse(null);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }
}