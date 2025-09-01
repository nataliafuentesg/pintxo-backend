package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.auth.AdminUserProvider;
import com.tasca.tasca_backend.dtos.LoginRequest;
import com.tasca.tasca_backend.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AdminUserProvider admin;
    private final JwtService jwt;

    public AuthController(AdminUserProvider admin, JwtService jwt){
        this.admin = admin; this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body){
        if (!admin.matches(body.getEmail(), body.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("ok", false, "error", "INVALID_CREDENTIALS"));
        }
        String token = jwt.generate(admin.getEmail(), Map.of("roles", List.of("ADMIN"), "name", admin.getName()));
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "token", token,
                "user", Map.of("email", admin.getEmail(), "name", admin.getName(), "roles", List.of("ADMIN"))
        ));
    }
}