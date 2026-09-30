package com.cesde.educlic.controller;

import com.cesde.educlic.entity.Usuario;
import com.cesde.educlic.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Usado por login.html.
// Body esperado: { "email": "cliente@tienda.com", "password": "1234" }
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario credenciales) {
        Usuario usuario = authService.login(credenciales.getEmail(), credenciales.getPassword());
        return ResponseEntity.ok(usuario);
    }
}