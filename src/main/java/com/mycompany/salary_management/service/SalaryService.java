package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Salary;
import com.mycompany.salary_management.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;

    public Salary createSalary(Salary salary){
        return salaryRepository.save(salary);
    }

    public Iterable<Salary> getAllSalary(){
        return salaryRepository.findAll();
    }

    public Salary getSalaryById(Long id){
        return salaryRepository.findById(id).orElse(null);
    }

    public Salary updateSalary(Long id, Salary salary){
        if (salaryRepository.existsById(id)){
            salary.setId(id);
            return salaryRepository.save(salary);
        }
        return null;
    }

    public void deleteSalary(Long id){
        salaryRepository.deleteById(id);
    }
}
