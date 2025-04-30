package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = Map.of(
                "success", success,
                "message", message,
                "data", data
        );
        return ResponseEntity.status(status).body(response);
    }

    //POST: Crée une nouvelle paie
    @PostMapping
    public ResponseEntity<Map<String, Object>> addPayroll(@RequestBody Payroll payroll) {
        try {
            Payroll savedPayroll = payrollService.createPayroll(payroll);
            return buildResponse(true, "Paie créée avec succès", savedPayroll, HttpStatus.CREATED);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la création de la paie: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    // GET : Toutes les paies
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPayrolls() {
        try {
            List<Payroll> payrolls = payrollService.getAllPayrolls();
            return buildResponse(true, "Paies récupérées avec succès", payrolls, HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des paies", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET : Paie par id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayrollById(@PathVariable Long id) {
        try {
            Payroll payroll = payrollService.getPayrollById(id);
            if (payroll != null) {
                return buildResponse(true, "Paie récupérée avec succès", payroll, HttpStatus.OK);
            } else {
                return buildResponse(false, "Paie non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération de la paie: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT : Mise à jour d'une paie existante
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePayroll(@PathVariable Long id, @RequestBody Payroll payroll) {
        try {
            Payroll updatedPayroll = payrollService.updatePayroll(id, payroll);
            if (updatedPayroll != null) {
                return buildResponse(true, "Paie mise à jour avec succès", updatedPayroll, HttpStatus.OK);
            } else {
                return buildResponse(false, "Paie non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour de la paie: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE : Supprimer une paie
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePayroll(@PathVariable Long id) {
        try {
            if (payrollService.existsById(id)) {
                payrollService.deletePayroll(id);
                return buildResponse(true, "Paie supprimée avec succès", null, HttpStatus.OK);
            } else {
                return buildResponse(false, "Paie non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression de la paie: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}