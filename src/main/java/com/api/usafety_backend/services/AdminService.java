package com.api.usafety_backend.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.exceptions.UsuarioNaoAutorizadoException;
import com.api.usafety_backend.exceptions.UsuarioNaoEncontradoException;
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

    public void alterarStatusUsuario(Usuario editor, Long id, boolean ativo) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (!optUsuario.isPresent()) {
            log.warn("Usuario nao encontrado para desabilitar");

            throw new UsuarioNaoEncontradoException("Usuario nao encontrado para desabilitar");
        }

        Usuario u = optUsuario.get();

        if (!editor.isAdmin() || u.isAdmin()) {
            throw new UsuarioNaoAutorizadoException("Requerente não autorizado para desabilitar o usuario "
                    + u.getUsername() + " ou o usuario é um administrador.");
        }

        log.info("Administrador desabilitando o usuario " + u.getUsername());

        try {
            u.setAtivo(ativo);

            usuarioRepository.save(u);
        } catch (Exception e) {
            log.error("Erro ao desabilitar o usuario " + u.getUsername(), e);

            throw new RuntimeException("Erro ao desabilitar o usuario " + u.getUsername());
        }
    }
}
