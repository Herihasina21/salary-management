package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.entity.Salary;
import com.mycompany.salary_management.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    //POST
    @PostMapping
    public Salary addSalary(@RequestBody Salary salary) {
        return salaryService.createSalary(salary);
    }

    //GET
    @GetMapping
    public Iterable<Salary> getAllSalary() {
        return salaryService.getAllSalary();
    }

    //GET par id
    @GetMapping("/{id}")
    public Salary getAllSalaryById(@PathVariable Long id) {
        return salaryService.getSalaryById(id);
    }

    //PUT Maj
    @PutMapping("/{id}")
    public Salary updateSalary(@PathVariable Long id ,@RequestBody Salary salary ) {
        return salaryService.updateSalary(id, salary);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public void deleteSalary(@PathVariable Long id){
        salaryService.deleteSalary(id);
    }
}
