package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);  // Garder le même ID lors de la mise à jour
            return employeeRepository.save(employee);  // Enregistrer l'employé mis à jour
        }
        return null;  // Si l'employé n'existe pas, retourner null
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
