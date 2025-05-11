package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.PayrollDTO;
import com.mycompany.salary_management.entity.Payroll;
import com.mycompany.salary_management.service.PayrollService;
import com.mycompany.salary_management.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            return buildResponse(true,
                    "Fiche de paie créée pour " + responseDTO.getEmployeeName(),
                    responseDTO,
                    HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return buildResponse(false, e.getMessage(), null, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return buildResponse(false, e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return buildResponse(false,
                    "Erreur technique lors de la création",
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
            PayrollDTO responseDTO = payrollService.toDTO(updatedPayroll);
            return buildResponse(true, "Fiche de paie mise à jour avec succès", responseDTO, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return buildResponse(false, e.getMessage(), null, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return buildResponse(false, e.getMessage(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return buildResponse(false,
                    "Erreur technique lors de la mise à jour",
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // DELETE : Supprimer une paie
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePayroll(@PathVariable Long id) {
        try {
            if (payrollService.existsById(id)) {
                payrollService.deletePayroll(id);
                return buildResponse(true, "Fiche de paie supprimée avec succès", null, HttpStatus.OK);
            } else {
                return buildResponse(false, "Paie non trouvée avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression de la paie: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> exportPayrollPdf(@PathVariable Long id) {
        try {
            byte[] pdf = payrollService.generatePayrollPdf(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payroll-" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la génération du PDF: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<?> sendPayroll(@PathVariable Long id) {
        try {
            payrollService.sendPayrollByEmail(id);
            return buildResponse(true, "Fiche de paie envoyée par mail avec succès", null, HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de l'envoi de la fiche de paie : " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-all")
    public ResponseEntity<?> sendAllPayrolls() {
        List<String> errors = payrollService.sendAllPayrollsByEmail();
        if (errors.isEmpty()) {
            return buildResponse(true, "Tous les emails de paie ont été envoyés avec succès", null, HttpStatus.OK);
        } else {
            return buildResponse(false, "Des erreurs sont survenues", errors, HttpStatus.MULTI_STATUS);
        }
    }
}
