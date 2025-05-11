package com.mycompany.salary_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
public class EmployeeDTO {
    Long id;
    String name;
    String firstName;
    String email;
    String phone;
    String address;
    String position;
    String contractType;
    Long departmentID;

    @JsonIgnore
    private LocalDate hireDate;

    // Format ISO par défaut (YYYY-MM-DD)
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    // Format français
    private static final DateTimeFormatter FR_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @JsonProperty("hireDate")
    public String getHireDateFormatted() {
        return hireDate != null ? hireDate.format(FR_FORMATTER) : null;
    }

    @JsonProperty("hireDate")
    public void setHireDateFormatted(String formatted) {
        if (formatted != null && !formatted.isBlank()) {
            try {
                // Essayer d'abord le format ISO
                this.hireDate = LocalDate.parse(formatted, ISO_FORMATTER);
            } catch (DateTimeParseException e1) {
                try {
                    // Si échec, essayer le format français
                    this.hireDate = LocalDate.parse(formatted, FR_FORMATTER);
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Format de date invalide. Utilisez YYYY-MM-DD ou dd/MM/yyyy");
                }
            }
        }
    }
}