package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.SalaryDTO;
import com.mycompany.salary_management.entity.Salary;
import com.mycompany.salary_management.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = Map.of(
                "success", success,
                "message", message,
                "data", data
        );
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addSalary(@RequestBody SalaryDTO salaryDTO) {
        try {
        if (salaryService.isSalaryLinkedToEmployee(salaryDTO.getEmployeeId())) {
                return buildResponse(false, "Cet employé est déjà lié à un salaire", null, HttpStatus.BAD_REQUEST);
            }
            Salary savedSalary = salaryService.createSalary(salaryDTO);
            return buildResponse(true, "Salaire ajouté avec succès", savedSalary, HttpStatus.CREATED);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de l'ajout du salaire: " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSalaries() {
        try {
            return buildResponse(true, "Salaires récupérés avec succès", salaryService.getAllSalary(), HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des salaires", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSalaryById(@PathVariable Long id) {
        try {
            Salary salary = salaryService.getSalaryById(id);
            if (salary != null) {
                return buildResponse(true, "Salaire récupéré avec succès", salary, HttpStatus.OK);
            } else {
                return buildResponse(false, "Salaire non trouvé avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération du salaire", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateSalary(@PathVariable Long id, @RequestBody SalaryDTO salary) {
        try {
            Salary updatedSalary = salaryService.updateSalary(id, salary);
            if (updatedSalary != null) {
                return buildResponse(true, "Salaire mis à jour avec succès", updatedSalary, HttpStatus.OK);
            } else {
                return buildResponse(false, "Salaire non trouvé", null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour du salaire: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteSalary(@PathVariable Long id) {
        try {
            if (salaryService.existsById(id)) {
                salaryService.deleteSalary(id);
                return buildResponse(true, "Salaire supprimé avec succès", null, HttpStatus.OK);
            } else {
                return buildResponse(false, "Salaire non trouvé avec l'ID: " + id, null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression du salaire: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
