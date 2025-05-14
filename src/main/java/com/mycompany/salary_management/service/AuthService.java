package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.JwtResponse;
import com.mycompany.salary_management.dto.LoginDTO;
import com.mycompany.salary_management.dto.RegisterDTO;
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
            extraClaims.put("username", user.getUsername()); // Ajoutez le username dans les claims

            var jwtToken = jwtService.generateToken(
                    extraClaims, // Passez les claims supplémentaires
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles("ADMIN") // ou "ADMIN" selon votre besoin
                            .build()
            );

            return new JwtResponse(jwtToken, user.getUsername()); // Retournez le username aussi

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(
                    UNAUTHORIZED,
                    "Email ou mot de passe incorrect"
            );
        }
    }
}