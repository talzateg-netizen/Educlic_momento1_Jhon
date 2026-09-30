package com.cesde.educlic.service;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Object usuarioRepository;

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
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (!usuario.isActivo()) {
            throw new BusinessException("El usuario se encuentra inactivo");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new BusinessException("Credenciales invalidas");
        }

        return usuario;
    }

    @SuppressWarnings("unchecked")
    private Usuario buscarUsuarioPorEmail(String email) {
        try {
            Method findByEmail = usuarioRepository.getClass().getMethod("findByEmail", String.class);
            Object resultado = findByEmail.invoke(usuarioRepository, email);

            if (resultado instanceof Optional<?>) {
                Optional<Usuario> usuarioOpt = (Optional<Usuario>) resultado;
                return usuarioOpt.orElseThrow(() -> new BusinessException("Credenciales invalidas"));
            }
        } catch (Exception e) {
            throw new BusinessException("Credenciales invalidas");
        }

        throw new BusinessException("Credenciales invalidas");
    }
}