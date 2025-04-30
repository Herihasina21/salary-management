package com.mycompany.salary_management.service;

import com.mycompany.salary_management.entity.Bonus;
import com.mycompany.salary_management.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BonusService {

    @Autowired
    private BonusRepository bonusRepository;

    public Bonus createBonus(Bonus bonus){
        return bonusRepository.save(bonus);
    }

    public List<Bonus> getAllBonus() {
        return (List<Bonus>) bonusRepository.findAll(); // Transformer Iterable en List
    }

    public Bonus getBonusById(Long id) {
        return bonusRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return bonusRepository.existsById(id);
    }

    public Bonus updateBonus(Long id, Bonus bonus) {
        if (bonusRepository.existsById(id)) {
            bonus.setId(id);
            return bonusRepository.save(bonus);
        }
        return null;
    }

    public void deleteBonus(Long id) {
        bonusRepository.deleteById(id);
    }
}