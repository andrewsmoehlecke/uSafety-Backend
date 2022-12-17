package com.api.usafety_backend.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.configs.UserPrincipal;
import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.TopicoFullDto;
import com.api.usafety_backend.services.TopicoService;
import com.api.usafety_backend.services.UsuarioService;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private UsuarioService usuarioService;

    private ModelMapper mapper = new ModelMapper();

    Logger log = LoggerFactory.getLogger(TopicoController.class);

    @PostMapping("/criar")
    public ResponseEntity<Void> criarTopico(
            UserPrincipal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("POST /topico/criar");
        log.info("Usuario " + principal.getUsername());

        try {
            topicoService.salvar(topicoDto);

            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            log.error("Erro ao criar tópico.", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> editarTopico(
            UserPrincipal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("PUT /topico/editar");

        try {
            Usuario u = usuarioService.buscarPorUsername(principal.getUsername());

            topicoService.editar(topicoDto, u);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao editar tópico.", e);
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(
            UserPrincipal principal,
            @PathVariable("id") Long idTopico) {
        log.info("DELETE /topico/deletar");

        try {
            Usuario u = usuarioService.buscarPorUsername(principal.getUsername());

            topicoService.deletar(idTopico, u);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao deletar tópico.", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/listarTopicos")
    public ResponseEntity<List<TopicoFullDto>> listarTopicos(
            UserPrincipal principal) {
        log.info("GET /topico/listarTopicos");
        log.info("Usuario " + principal.getUsername());

        try {
            return ResponseEntity.ok(topicoService.buscarTodos());
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoFullDto> buscarTopicoPorId(
            UserPrincipal principal,
            @PathVariable("id") Long id) {
        log.info("GET /topico/" + id);
        log.info("Usuario " + principal.getUsername());

        try {
            return ResponseEntity.ok(new TopicoFullDto(topicoService.buscarPorId(id)));
        } catch (Exception e) {
            log.error("Erro ao buscar tópico por id.", e);
            return ResponseEntity.status(500).build();
        }
    }
}
