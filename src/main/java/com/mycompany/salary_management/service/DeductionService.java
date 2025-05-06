package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.DeductionDTO;
import com.mycompany.salary_management.entity.Deduction;
import com.mycompany.salary_management.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeductionService {

    @Autowired
    private DeductionRepository deductionRepository;

    // Create
    public DeductionDTO createDeduction(DeductionDTO deductionDTO) {
        Deduction deduction = fromDTO(deductionDTO);
        Deduction saved = deductionRepository.save(deduction);
        return toDeductionDTO(saved);
    }

    // Get all
    public List<DeductionDTO> getAllDeduction() {
        return deductionRepository.findAll().stream()
                .map(DeductionService::toDeductionDTO) // ✅ Appel static correct
                .collect(Collectors.toList());
    }

    // Get by ID
    public DeductionDTO getDeductionById(Long id) {
        return deductionRepository.findById(id)
                .map(DeductionService::toDeductionDTO) // ✅ Appel static correct
                .orElse(null);
    }

    // Exists
    public boolean existsById(Long id) {
        return deductionRepository.existsById(id);
    }

    // Update
    public DeductionDTO updateDeduction(Long id, DeductionDTO dto) {
        if (deductionRepository.existsById(id)) {
            Deduction deduction = fromDTO(dto);
            deduction.setId(id);
            Deduction updated = deductionRepository.save(deduction);
            return toDeductionDTO(updated);
        }
        return null;
    }

    // Delete
    public void deleteDeduction(Long id) {
        deductionRepository.deleteById(id);
    }

    // Mapping to DTO
    public static DeductionDTO toDeductionDTO(Deduction deduction) {
        DeductionDTO dto = new DeductionDTO();
        dto.setId(deduction.getId());
        dto.setType(deduction.getType());
        dto.setAmount(deduction.getAmount());
        return dto;
    }

    // Mapping from DTO
    public static Deduction fromDTO(DeductionDTO dto) {
        Deduction deduction = new Deduction();
        deduction.setType(dto.getType());
        deduction.setAmount(dto.getAmount());
        return deduction;
    }
}

