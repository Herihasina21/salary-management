package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Bonus;
import com.mycompany.salary_management.service.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    //POST: Crée un nouveau bonus
    @PostMapping
    public Bonus addBonus(@RequestBody Bonus bonus) {
        return bonusService.createBonus(bonus);
    }

    // GET
    @GetMapping
    public Iterable<Bonus> getAllBonus() {
        return bonusService.getAllBonus();
    }

    //GET : Bonus par id
    @GetMapping("/{id}")
    public Bonus getBonusById(@PathVariable Long id) {
        return bonusService.getBonusById(id);
    }

    //PUT
    @PutMapping("/{id}")
    public Bonus updateBonus(@PathVariable Long id ,@RequestBody Bonus bonus) {
        return bonusService.updateBonus(id, bonus);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteBonus(@PathVariable Long id){
        bonusService.deleteBonus(id);
    }
}
