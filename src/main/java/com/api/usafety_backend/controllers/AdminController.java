package com.api.usafety_backend.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.AlterarSenhaDto;
import com.api.usafety_backend.entities.dtos.RespostaSimplesDto;
import com.api.usafety_backend.entities.dtos.UsuarioDto;
import com.api.usafety_backend.exceptions.UsuarioNaoAutorizadoException;
import com.api.usafety_backend.services.UsuarioService;
import com.api.usafety_backend.util.Constantes;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    private final Constantes constantes = new Constantes();

    Logger log = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/criar")
    public ResponseEntity<RespostaSimplesDto> criarUsuario(
            Principal principal,
            @RequestBody UsuarioDto dto) {
        log.info("POST /admin/criar");

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());

        if (usuario.isAdmin()) {
            log.info("Criando usuário " + dto.getUsername());

            usuarioService.criar(new Usuario(dto));

            return ResponseEntity.ok(new RespostaSimplesDto(constantes.USUARIO_CRIADO));
        } else {
            log.warn("Acesso negado ao tentar criar outro usuario para: " + usuario.getUsername());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/alterarSenhaDoUsuario/{id}")
    public ResponseEntity<RespostaSimplesDto> alterarSenha(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestBody AlterarSenhaDto dto) {
        log.info("PUT /usuario/alterarSenhaDoUsuario/" + id);

        try {
            Usuario admin = usuarioService.buscarPorUsername(principal.getName());

            return ResponseEntity.ok(new RespostaSimplesDto(usuarioService.adminAlterarSenhaDoUsuario(admin, dto, id)));
        } catch (UsuarioNaoAutorizadoException e) {
            log.error("Usuário não autorizado", e);

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
