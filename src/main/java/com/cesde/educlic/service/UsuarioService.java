package com.cesde.educlic.service;

import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cesde.educlic.model.entity.Usua;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return buscarEntidad(id);
    }

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con ese email");
        }
        usuario.setId(null);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario cambios) {
        Usuario usuario = buscarEntidad(id);

        if (!usuario.getEmail().equalsIgnoreCase(cambios.getEmail())
                && usuarioRepository.existsByEmail(cambios.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con ese email");
        }

        usuario.setNombre(cambios.getNombre());
        usuario.setEmail(cambios.getEmail());
        usuario.setPassword(cambios.getPassword());
        usuario.setTelefono(cambios.getTelefono());
        usuario.setRol(cambios.getRol());

        return usuarioRepository.save(usuario);
    }

    public void desactivar(Long id) {
        Usuario usuario = buscarEntidad(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }
}