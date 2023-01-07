package com.api.usafety_backend.controllers;

import java.security.Principal;
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

import com.api.usafety_backend.entities.Topico.Tipos;
import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.TopicoFullDto;
import com.api.usafety_backend.exceptions.UsuarioNaoAutorizadoException;
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

    @PostMapping("/criarConteudo")
    public ResponseEntity<Void> criarConteudo(
            Principal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("POST /topico/criarConteudo");
        log.info("Usuario " + principal.getName());

        try {
            topicoService.salvar(topicoDto, Tipos.CONTEUDO, principal.getName());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UsuarioNaoAutorizadoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Erro ao criar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/criarDuvida")
    public ResponseEntity<Void> criarDuvida(
            Principal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("POST /topico/criarDuvida");
        log.info("Usuario " + principal.getName());

        try {
            topicoService.salvar(topicoDto, Tipos.DUVIDA);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Erro ao criar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/criarDiscussao")
    public ResponseEntity<Void> criarDiscussao(
            Principal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("POST /topico/criarDiscussao");
        log.info("Usuario " + principal.getName());

        try {
            topicoService.salvar(topicoDto, Tipos.DISCUSSAO);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Erro ao criar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> editarTopico(
            Principal principal,
            @RequestBody TopicoFullDto topicoDto) {
        log.info("PUT /topico/editar");

        try {
            Usuario u = usuarioService.buscarPorUsername(principal.getName());

            topicoService.editar(topicoDto, u);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao editar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(
            Principal principal,
            @PathVariable("id") Long idTopico) {
        log.info("DELETE /topico/deletar");

        try {
            Usuario u = usuarioService.buscarPorUsername(principal.getName());

            topicoService.deletar(idTopico, u);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao deletar tópico.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarDuvidas")
    public ResponseEntity<List<TopicoFullDto>> buscarDuvidas(
            Principal principal) {
        log.info("GET /topico/buscarDuvidas");
        log.info("Usuario " + principal.getName());

        try {
            return ResponseEntity.ok(topicoService.buscarTopicosPorTipo(Tipos.DUVIDA));
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarDiscussoes")
    public ResponseEntity<List<TopicoFullDto>> buscarTodasDiscussoes(
            Principal principal) {
        log.info("GET /topico/buscarDiscussoes");
        log.info("Usuario " + principal.getName());

        try {
            return ResponseEntity.ok(topicoService.buscarTopicosPorTipo(Tipos.DISCUSSAO));
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarConteudos")
    public ResponseEntity<List<TopicoFullDto>> buscarConteudos(
            Principal principal) {
        log.info("GET /topico/buscarConteudos");
        log.info("Usuario " + principal.getName());

        try {
            return ResponseEntity.ok(topicoService.buscarTopicosPorTipo(Tipos.CONTEUDO));
        } catch (Exception e) {
            log.error("Erro ao listar tópicos.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoFullDto> buscarTopicoPorId(
            Principal principal,
            @PathVariable("id") Long id) {
        log.info("GET /topico/" + id);
        log.info("Usuario " + principal.getName());

        try {
            return ResponseEntity.ok(new TopicoFullDto(topicoService.buscarPorId(id)));
        } catch (Exception e) {
            log.error("Erro ao buscar tópico por id.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
