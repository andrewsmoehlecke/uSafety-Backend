package com.api.usafety_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.repositories.UsuarioRepository;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    Logger log = LoggerFactory.getLogger(AdminService.class);

    public void criarUsuario(Usuario usuario) {
        log.info("Administrador criando o usuario " + usuario.getUsername());

        try {
            usuario.addCargo(Usuario.Cargos.USUARIO);

            usuarioRepository.save(usuario);
        } catch (Exception e) {
            log.error("Erro ao criar o usuario " + usuario.getUsername(), e);

            throw new RuntimeException("Erro ao criar o usuario " + usuario.getUsername());
        }
    }

    public void atualizarUsuario(Usuario u) {
        log.info("Administrador atualizando o usuario " + u.getUsername());

        try {
            usuarioRepository.save(u);
        } catch (Exception e) {
            log.error("Erro ao atualizar o usuario " + u.getUsername(), e);

            throw new RuntimeException("Erro ao atualizar o usuario " + u.getUsername());
        }
    }
}
