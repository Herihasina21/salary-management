package com.mycompany.salary_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
public class PayrollDTO {
    private Long id;

    @JsonIgnore
    private LocalDate periodStart;

    @JsonIgnore
    private LocalDate periodEnd;

    private double grossSalary;
    private double netSalary;

    private List<BonusDTO> bonuses;
    private List<DeductionDTO> deductions;

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;

    // Format ISO par défaut (YYYY-MM-DD)
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    // Format français
    private static final DateTimeFormatter FR_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @JsonProperty("periodStart")
    public String getPeriodStartFormatted() {
        return periodStart != null ? periodStart.format(FR_FORMATTER) : null;
    }

    @JsonProperty("periodEnd")
    public String getPeriodEndFormatted() {
        return periodEnd != null ? periodEnd.format(FR_FORMATTER) : null;
    }

    @JsonProperty("periodStart")
    public void setPeriodStartFormatted(String formatted) {
        if (formatted != null && !formatted.isBlank()) {
            try {
                // Essayer d'abord le format ISO
                this.periodStart = LocalDate.parse(formatted, ISO_FORMATTER);
            } catch (DateTimeParseException e1) {
                try {
                    // Si échec, essayer le format français
                    this.periodStart = LocalDate.parse(formatted, FR_FORMATTER);
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Format de date invalide. Utilisez YYYY-MM-DD ou dd/MM/yyyy");
                }
            }
        }
    }

    @JsonProperty("periodEnd")
    public void setPeriodEndFormatted(String formatted) {
        if (formatted != null && !formatted.isBlank()) {
            try {
                // Essayer d'abord le format ISO
                this.periodEnd = LocalDate.parse(formatted, ISO_FORMATTER);
            } catch (DateTimeParseException e1) {
                try {
                    // Si échec, essayer le format français
                    this.periodEnd = LocalDate.parse(formatted, FR_FORMATTER);
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Format de date invalide. Utilisez YYYY-MM-DD ou dd/MM/yyyy");
                }
            }
        }
    }
}
