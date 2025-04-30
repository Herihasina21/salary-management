package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.entity.Department;
import com.mycompany.salary_management.service.DepartmentService;
import com.mycompany.salary_management.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService; // Injection de EmployeeService

    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = Map.of(
                "success", success,
                "message", message,
                "data", data
        );
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDepartment(@RequestBody Department department) {
        try {
            Department saved = departmentService.createDepartment(department);
            return buildResponse(true, "Département créé avec succès", saved, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return buildResponse(false, "Erreur lors de la création du département : " + e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDepartments() {
        try {
            return buildResponse(true, "Départements récupérés avec succès", departmentService.getAllDepartments(), HttpStatus.OK);
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération des départements", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDepartmentById(@PathVariable Long id) {
        try {
            Department department = departmentService.getDepartmentById(id);
            if (department != null) {
                return buildResponse(true, "Département trouvé", department, HttpStatus.OK);
            } else {
                return buildResponse(false, "Département non trouvé", null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la récupération du département", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        try {
            Department updated = departmentService.updateDepartment(id, department);
            if (updated != null) {
                return buildResponse(true, "Département mis à jour avec succès", updated, HttpStatus.OK);
            } else {
                return buildResponse(false, "Département non trouvé", null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la mise à jour du département : " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable Long id) {
        try {
            if (!departmentService.existsById(id)) {
                return buildResponse(false, "Département non trouvé avec l'ID : " + id, Map.of(), HttpStatus.NOT_FOUND); // Modification ici
            }

            if (employeeService.isEmployeeLinkedToDepartment(id)) {
                return buildResponse(false, "Ce département est lié à des employés. La suppression n'est pas autorisée.", Map.of(), HttpStatus.BAD_REQUEST); // Modification ici
            }

            departmentService.deleteDepartment(id);
            return buildResponse(true, "Département supprimé avec succès", Map.of(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return buildResponse(false, e.getMessage(), Map.of(), HttpStatus.NOT_FOUND); // Modification ici
        } catch (Exception e) {
            return buildResponse(false, "Erreur lors de la suppression du département : " + e.getMessage(), Map.of(), HttpStatus.INTERNAL_SERVER_ERROR); // Modification ici
        }
    }
}