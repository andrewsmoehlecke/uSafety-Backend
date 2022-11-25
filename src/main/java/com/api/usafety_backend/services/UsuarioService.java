package com.api.usafety_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

}
