package com.cesde.educlic.service;

import com.cesde.educlic.entity.Usuario;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Login simple: recibe un Usuario con solo email+password llenos
     * (el resto de campos vienen null y no importan), valida contra la BD
     * y devuelve el Usuario encontrado. La contrasena no se devuelve en el
     * JSON de salida gracias a @JsonProperty(WRITE_ONLY) en la entidad.
     *
     * NOTA: cuando se agregue spring-security, reemplazar la comparacion
     * de password en texto plano por BCryptPasswordEncoder.matches(...).
     */
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Credenciales invalidas"));

        if (!usuario.isActivo()) {
            throw new BusinessException("El usuario se encuentra inactivo");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new BusinessException("Credenciales invalidas");
        }

        return usuario;
    }
}