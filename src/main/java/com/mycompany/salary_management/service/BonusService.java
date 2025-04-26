package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Bonus;
import com.mycompany.salary_management.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BonusService {

    @Autowired
    private BonusRepository bonusRepository;

    public Bonus createBonus(Bonus bonus){
        return bonusRepository.save(bonus);
    }

    public Iterable<Bonus> getAllBonus() {
        return bonusRepository.findAll();
    }

    public Bonus getBonusById(Long id) {
        return bonusRepository.findById(id).orElse(null);
    }

    public Bonus updateBonus(Long id, Bonus bonus) {
        if (bonusRepository.existsById(id)) {
            bonus.setId(id);  // Garder le même ID lors de la mise à jour
            return bonusRepository.save(bonus);  // Enregistrer l'employé mis à jour
        }
        return null;  // Si l'employé n'existe pas, retourner null
    }

    public void deleteBonus(Long id) {
        bonusRepository.deleteById(id);
    }

}
