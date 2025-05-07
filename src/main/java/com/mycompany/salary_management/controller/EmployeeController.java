package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.EmployeeDTO;
import com.mycompany.salary_management.entity.Employee;
import com.mycompany.salary_management.service.EmployeeService;
import com.mycompany.salary_management.service.DepartmentService; // Ajout de DepartmentService
import com.mycompany.salary_management.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService; // Injection de DepartmentService

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
    public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee savedEmployee = employeeService.createEmployee(employeeDTO);
            return buildResponse(true, "Employé ajouté avec succès", savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildResponse(false, "Erreur lors de l'ajout de l'employé: " + e.getMessage(), Map.of(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        try {
            return buildResponse(true, "Employés récupérés avec succès", employeeService.getAllEmployees(), HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des employés", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee updated = employeeService.updateEmployee(id, employeeDTO);
            if (updated != null) {
                return buildResponse(true, "Employé mis à jour avec succès", updated, HttpStatus.OK);
            } else {
                return buildResponse(false, "Employé non trouvé", null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour : " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable Long id) {
        try {
            if (!employeeService.existsById(id)) {
                return buildResponse(false, "Employé non trouvé avec l'ID: " + id,  Map.of(), HttpStatus.NOT_FOUND);
            }

            if (salaryService.isSalaryLinkedToEmployee(id)) {
                return buildResponse(false, "Cet employé est lié à des salaires. La suppression n'est pas autorisée.", Map.of(), HttpStatus.BAD_REQUEST); // Modification ici
            }

            employeeService.deleteEmployee(id);
            return buildResponse(true, "Employé supprimé avec succès",  Map.of(), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return buildResponse(false, e.getMessage(), Map.of(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression de l'employé: " + e.getMessage(),  Map.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
