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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.ComentarioDto;
import com.api.usafety_backend.entities.dtos.CriarComentarioDto;
import com.api.usafety_backend.services.ComentarioService;
import com.api.usafety_backend.services.UsuarioService;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioService usuarioService;

    Logger log = LoggerFactory.getLogger(ComentarioController.class);

    @PostMapping("/criar")
    public ResponseEntity<Void> criarComentario(
            Principal principal,
            @RequestBody CriarComentarioDto dto) {
        log.info("POST /comentario/criar");

        Usuario autor = usuarioService.buscarPorUsername(principal.getName());

        comentarioService.criar(dto, autor);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/editar")
    public ResponseEntity<Void> editarComentario(
            Principal principal,
            @RequestBody CriarComentarioDto dto) {
        log.info("PUT /comentario/editar");

        Usuario autor = usuarioService.buscarPorUsername(principal.getName());

        comentarioService.editar(dto.getId(), dto.getConteudo(), autor);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<Void> excluirComentario(
            Principal principal,
            @RequestParam Long id) {
        log.info("DELETE /comentario/excluir");

        Usuario autor = usuarioService.buscarPorUsername(principal.getName());

        comentarioService.excluir(id, autor);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/buscarPorTopico")
    public ResponseEntity<List<ComentarioDto>> buscarPorTopico(
            @RequestParam Long id) {
        log.info("GET /comentario/buscarPorTopico");

        return ResponseEntity.ok(comentarioService.buscarPorTopico(id));
    }

}
