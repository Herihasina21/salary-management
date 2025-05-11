package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.BonusDTO;
import com.mycompany.salary_management.entity.Bonus;
import com.mycompany.salary_management.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BonusService {

    @Autowired
    private BonusRepository bonusRepository;

    // Create
    public BonusDTO createBonus(BonusDTO bonusDTO) {
        Bonus bonus = fromDTO(bonusDTO);
        Bonus saved = bonusRepository.save(bonus);
        return toBonusDTO(saved);
    }

    // Get All
    public List<BonusDTO> getAllBonus() {
        return bonusRepository.findAll().stream()
                .map(BonusService::toBonusDTO)
                .collect(Collectors.toList());
    }

    // Get By ID
    public BonusDTO getBonusById(Long id) {
        return bonusRepository.findById(id)
                .map(BonusService::toBonusDTO)
                .orElse(null);
    }

    // Exists
    public boolean existsById(Long id) {
        return bonusRepository.existsById(id);
    }

    // Update
    public BonusDTO updateBonus(Long id, BonusDTO bonusDTO) {
        if (bonusRepository.existsById(id)) {
            Bonus bonus = fromDTO(bonusDTO);
            bonus.setId(id);
            Bonus updated = bonusRepository.save(bonus);
            return toBonusDTO(updated);
        }
        return null;
    }

    // Delete
    public void deleteBonus(Long id) {
        bonusRepository.deleteById(id);
    }

    // Mapping to DTO
    public static BonusDTO toBonusDTO(Bonus bonus) {
        BonusDTO dto = new BonusDTO();
        dto.setId(bonus.getId());
        dto.setType(bonus.getType());
        dto.setAmount(bonus.getAmount());
        return dto;
    }

    // Mapping from DTO
    public static Bonus fromDTO(BonusDTO dto) {
        Bonus bonus = new Bonus();
        bonus.setType(dto.getType());
        bonus.setAmount(dto.getAmount());
        return bonus;
    }

    public boolean isBonusAssociatedWithPayroll(Long id) {
        Bonus bonus = bonusRepository.findById(id).orElse(null);
        return bonus != null && !bonus.getPayrolls().isEmpty();
    }
}

