package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employé non trouvé avec l'ID : " + id));
    }

    public Employee updateEmployee(Long id, Employee employee) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    employee.setId(id);
                    return employeeRepository.save(employee);
                })
                .orElse(null);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    public boolean isEmployeeLinkedToDepartment(Long departmentId) {
        return !employeeRepository.findByDepartmentId(departmentId).isEmpty();
    }
}