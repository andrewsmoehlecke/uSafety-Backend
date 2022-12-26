package com.api.usafety_backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.api.usafety_backend.util.Constantes;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private UsuarioService usuarioService;

    private Constantes constantes = new Constantes();

    Logger log = LoggerFactory.getLogger(TopicoController.class);

    @PostMapping("/criar")
    public ResponseEntity<Void> criarTopico(
            UserPrincipal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("POST /topico/criar");
        log.info("Usuario " + principal.getUsername());

        try {
            topicoService.salvar(topicoDto);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Erro ao criar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarTodasDuvidas")
    public ResponseEntity<List<TopicoFullDto>> buscarTodasDuvidas(
            UserPrincipal principal) {
        log.info("GET /topico/buscarTodasDuvidas");
        log.info("Usuario " + principal.getUsername());

        try {
            return ResponseEntity.ok(topicoService.buscarTopicosPorTipo(constantes.TOPICO_DUVIDA));
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarTodasDiscussoes")
    public ResponseEntity<List<TopicoFullDto>> buscarTodasDiscussoes(
            UserPrincipal principal) {
        log.info("GET /topico/buscarTodasDiscussoes");
        log.info("Usuario " + principal.getUsername());

        try {
            return ResponseEntity.ok(topicoService.buscarTopicosPorTipo(constantes.TOPICO_DISCUSSAO));
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
