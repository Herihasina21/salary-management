package com.mycompany.salary_management.controller;

import com.mycompany.salary_management.dto.*;
import com.mycompany.salary_management.entity.User;
import com.mycompany.salary_management.security.JwtService;
import com.mycompany.salary_management.security.UserDetailsServiceImpl;
import com.mycompany.salary_management.service.AuthService;
import com.mycompany.salary_management.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          TokenBlacklistService tokenBlacklistService,
                          UserDetailsServiceImpl userDetailsService,
                          JwtService jwtService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public static ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestBody UpdateProfileDTO updateDTO,
            Principal principal
    ) {
        try {
            User updatedUser = authService.updateProfile(principal.getName(), updateDTO);

            // Régénérer le token avec les nouvelles informations
            UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getEmail());
            String newToken = jwtService.generateToken(userDetails);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("user", updatedUser);
            responseData.put("token", newToken);

            return buildResponse(
                    true,
                    "Profil mis à jour avec succès",
                    responseData,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    "Échec de la mise à jour: " + e.getMessage(),
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            Principal principal
    ) {
        try {
            authService.changePassword(principal.getName(), changePasswordDTO);
            return buildResponse(
                    true,
                    "Mot de passe changé avec succès",
                    null,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    "Mot de passe actuel incorrect",
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
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

            // Créez la structure de données pour la réponse
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", jwtResponse.getToken());
            responseData.put("username", jwtResponse.getUsername());

            return buildResponse(
                    true,
                    "Connexion réussie",
                    responseData, // Utilisez la nouvelle structure
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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        String token = extractToken(request);

        if (token == null) {
            return buildResponse(
                    false,
                    "Aucun token fourni",
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            tokenBlacklistService.addToBlacklist(token);
            return buildResponse(
                    true,
                    "Déconnexion réussie",
                    null,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    "Échec de la déconnexion: " + e.getMessage(),
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        try {
            authService.initiatePasswordReset(forgotPasswordDTO.getEmail());
            return buildResponse(
                    true,
                    "Un email de réinitialisation a été envoyé",
                    null,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    e.getMessage(),
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            authService.completePasswordReset(
                    resetPasswordDTO.getToken(),
                    resetPasswordDTO.getNewPassword()
            );
            return buildResponse(
                    true,
                    "Mot de passe réinitialisé avec succès",
                    null,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return buildResponse(
                    false,
                    e.getMessage(),
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}