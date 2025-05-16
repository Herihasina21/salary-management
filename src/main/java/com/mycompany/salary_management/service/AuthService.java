package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.*;
import com.mycompany.salary_management.entity.User;
import com.mycompany.salary_management.repository.UserRepository;
import com.mycompany.salary_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Un utilisateur avec cet email existe déjà"
            );
        }

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Ce nom d'utilisateur est déjà pris"
            );
        }

        var user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();

        userRepository.save(user);
        return "Inscription réussie";
    }

    public JwtResponse login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            var user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

            // Créez une map pour les claims supplémentaires
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("username", user.getUsername());

            var jwtToken = jwtService.generateToken(
                    extraClaims, // Passez les claims supplémentaires
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles("ADMIN")
                            .build()
            );

            return new JwtResponse(jwtToken, user.getUsername());

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(
                    UNAUTHORIZED,
                    "Email ou mot de passe incorrect"
            );
        }
    }

    public User updateProfile(String email, UpdateProfileDTO updateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Un utilisateur avec cet email existe déjà"
            );
        }

        if (!user.getUsername().equals(updateDTO.getUsername()) &&
                userRepository.existsByUsername(updateDTO.getUsername())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Ce nom d'utilisateur est déjà pris"
            );
        }

        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());

        return userRepository.save(user);
    }

    public void changePassword(String email, ChangePasswordDTO changePasswordDTO) {
        // Accès aux propriétés via les getters générés par Lombok
        String currentPassword = changePasswordDTO.getCurrentPassword();
        String newPassword = changePasswordDTO.getNewPassword();

        // Validation
        if (newPassword == null || newPassword.length() < 8) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Le mot de passe doit contenir au moins 8 caractères"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Mot de passe actuel incorrect"
            );
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}