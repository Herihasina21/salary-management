package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Deduction;
import com.mycompany.salary_management.service.DeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deductions")
public class DeductionController {

    @Autowired
    private DeductionService deductionService;

    //POST: Crée un nouveau bonus
    @PostMapping
    public Deduction addDeduction(@RequestBody Deduction deduction) {
        return deductionService.createDeduction(deduction);
    }

    // GET
    @GetMapping
    public Iterable<Deduction> getAllDeduction() {
        return deductionService.getAllDeduction();
    }

    //GET : Bonus par id
    @GetMapping("/{id}")
    public Deduction getDeductionById(@PathVariable Long id) {
        return deductionService.getDeductionById(id);
    }

    //PUT
    @PutMapping("/{id}")
    public Deduction updateDeduction(@PathVariable Long id ,@RequestBody Deduction deduction) {
        return deductionService.updateDeduction(id, deduction);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteDeduction(@PathVariable Long id){
        deductionService.deleteDeduction(id);
    }
}
