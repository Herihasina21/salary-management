package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.EmployeeDTO;
import com.mycompany.salary_management.dto.SalaryDTO;
import com.mycompany.salary_management.entity.Department;
import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.entity.Salary;
import com.mycompany.salary_management.repository.EmployeeRepository;
import com.mycompany.salary_management.repository.SalaryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Salary createSalary(SalaryDTO salaryDTO){
        Salary salary = fromDTO(salaryDTO, Optional.empty());
        return salaryRepository.save(salary);
    }

    public List<Salary> getAllSalary(){
        return salaryRepository.findAll();
    }

    public Salary getSalaryById(Long id){
        return salaryRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return salaryRepository.existsById(id);
    }

    public Salary updateSalary(Long id, SalaryDTO salaryDTO){
        Salary existingSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salary introuvable avec l'id : " + id));

        Salary updated = fromDTO(salaryDTO, Optional.of(existingSalary));
        return salaryRepository.save(updated);
    }

    public void deleteSalary(Long id){
        salaryRepository.deleteById(id);
    }

    private Salary fromDTO(SalaryDTO dto, Optional<Salary> existing) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employé introuvable avec l'id : " + dto.getEmployeeId()));

        Salary salary = existing.orElse(new Salary());

        salary.setBaseSalary(dto.getBaseSalary());
        salary.setEmployee(employee);

        return salary;
    }
}