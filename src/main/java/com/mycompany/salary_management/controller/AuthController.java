package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.JwtResponse;
import com.mycompany.salary_management.dto.LoginDTO;
import com.mycompany.salary_management.dto.RegisterDTO;
import com.mycompany.salary_management.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterDTO registerDTO) {
        try {
            String result = authService.register(registerDTO);
            return buildResponse(
                    true,
                    "Inscription réussie",
                    result,
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    "Échec de l'inscription: " + e.getMessage(),
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        try {
            JwtResponse jwtResponse = authService.login(loginDTO);
            return buildResponse(
                    true,
                    "Connexion réussie",
                    jwtResponse,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    "Échec de la connexion: " + e.getMessage(),
                    null,
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}