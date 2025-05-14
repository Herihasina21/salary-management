package com.mycompany.salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username; // Ajoutez ce champ

    // Si vous voulez garder la compatibilité avec l'existant
    public JwtResponse(String token) {
        this.token = token;
        this.username = null;
    }
}