package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    //POST: Crée un nouveau Payroll
    @PostMapping
    public Payroll addPayroll(@RequestBody Payroll payroll) {
        return payrollService.createPayroll(payroll);
    }

    // GET : Employee retra
    @GetMapping
    public Iterable<Payroll> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }

    //GET : Payroll par id
    @GetMapping("/{id}")
    public Payroll getPayrollById(@PathVariable Long id) {
        return payrollService.getPayrollById(id);
    }

    // PUT : Maj d'un Payroll existant
    @PutMapping("/{id}")
    public  Payroll updatePayroll(@PathVariable Long id ,@RequestBody Payroll payroll) {
        return payrollService.updatePayroll(id, payroll);
    }

    // DELETE : Supprimer un Payroll
    @DeleteMapping("/{id}")
    public void deletePayroll(@PathVariable Long id){
        payrollService.deletePayroll(id);
    }
}
