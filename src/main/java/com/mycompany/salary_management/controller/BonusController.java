package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.BonusDTO;
import com.mycompany.salary_management.entity.Bonus;
import com.mycompany.salary_management.service.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = Map.of(
                "success", success,
                "message", message,
                "data", data
        );
        return ResponseEntity.status(status).body(response);
    }

    //POST: Crée un nouveau bonus
    @PostMapping
    public ResponseEntity<Map<String, Object>> addBonus(@RequestBody BonusDTO bonusDTO) {
        try {
            BonusDTO savedBonus = bonusService.createBonus(bonusDTO);
            return buildResponse(true, "Bonus ajouté avec succès", savedBonus, HttpStatus.CREATED);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de l'ajout du bonus: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    // GET
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBonus() {
        try {
            return buildResponse(true, "Bonus récupérés avec succès", bonusService.getAllBonus(), HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des bonus", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET : Bonus par id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBonusById(@PathVariable Long id) {
        try {
            BonusDTO bonus = bonusService.getBonusById(id);
            if (bonus != null) {
                return buildResponse(true, "Bonus récupéré avec succès", bonus, HttpStatus.OK);
            } else {
                return buildResponse(false, "Bonus non trouvé avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération du bonus: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBonus(@PathVariable Long id, @RequestBody BonusDTO bonus) {
        try {
            BonusDTO updatedBonus = bonusService.updateBonus(id, bonus);
            if (updatedBonus != null) {
                return buildResponse(true, "Bonus mis à jour avec succès", updatedBonus, HttpStatus.OK);
            } else {
                return buildResponse(false, "Bonus non trouvé avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour du bonus: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBonus(@PathVariable Long id) {
        try {
            if (bonusService.existsById(id)) {
                bonusService.deleteBonus(id);
                return buildResponse(true, "Bonus supprimé avec succès", null, HttpStatus.OK);
            } else {
                return buildResponse(false, "Bonus non trouvé avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression du bonus: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
