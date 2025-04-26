package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Deduction;
import com.mycompany.salary_management.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeductionService {
    @Autowired
    private DeductionRepository deductionRepository;

    public Deduction createDeduction(Deduction deduction){
        return deductionRepository.save(deduction);
    }

    public Iterable<Deduction> getAllDeduction() {
        return deductionRepository.findAll();
    }

    public Deduction getDeductionById(Long id) {
        return deductionRepository.findById(id).orElse(null);
    }

    public Deduction updateDeduction(Long id, Deduction deduction) {
        if (deductionRepository.existsById(id)) {
            deduction.setId(id);  // Garder le même ID lors de la mise à jour
            return deductionRepository.save(deduction);  // Enregistrer l'employé mis à jour
        }
        return null;  // Si l'employé n'existe pas, retourner null
    }

    public void deleteDeduction(Long id) {
        deductionRepository.deleteById(id);
    }


}
