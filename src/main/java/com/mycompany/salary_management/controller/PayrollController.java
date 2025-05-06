package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.PayrollDTO;
import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPayroll(@RequestBody PayrollDTO payrollDTO) {
        try {
            Payroll createdPayroll = payrollService.createPayroll(payrollDTO);
            PayrollDTO responseDTO = payrollService.toDTO(createdPayroll);
            return buildResponse(true, "Paie créée avec succès", responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la création de la paie: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    // GET : Toutes les paies
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPayrolls() {
        try {
            List<PayrollDTO> payrolls = payrollService.getAllPayrolls();
            return buildResponse(true, "Paies récupérées avec succès", payrolls, HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des paies", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET : Paie par id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayrollById(@PathVariable Long id) {
        try {
            PayrollDTO payrollDTO = payrollService.getPayrollById(id);
            if (payrollDTO != null) {
                return buildResponse(true, "Paie récupérée avec succès", payrollDTO, HttpStatus.OK);
            } else {
                return buildResponse(false, "Paie non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération de la paie: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT : Mise à jour d'une paie existante
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePayroll(@PathVariable Long id, @RequestBody PayrollDTO dto) {
        try {
            Payroll updatedPayroll = payrollService.updatePayroll(id, dto);
            if (updatedPayroll != null) {
                PayrollDTO responseDTO = payrollService.toDTO(updatedPayroll);
                return buildResponse(true, "Paie mise à jour avec succès", responseDTO, HttpStatus.OK);
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
