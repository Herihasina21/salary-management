package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Deduction;
import com.mycompany.salary_management.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeductionService {
    @Autowired
    private DeductionRepository deductionRepository;

    public Deduction createDeduction(Deduction deduction){
        return deductionRepository.save(deduction);
    }

    public List<Deduction> getAllDeduction() {
        return (List<Deduction>) deductionRepository.findAll();
    }

    public Deduction getDeductionById(Long id) {
        return deductionRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return deductionRepository.existsById(id);
    }

    public Deduction updateDeduction(Long id, Deduction deduction) {
        if (deductionRepository.existsById(id)) {
            deduction.setId(id);
            return deductionRepository.save(deduction);
        }
        return null;
    }

    public void deleteDeduction(Long id) {
        deductionRepository.deleteById(id);
    }
}