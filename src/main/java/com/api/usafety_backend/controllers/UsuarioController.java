package com.api.usafety_backend.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.TokenDto;
import com.api.usafety_backend.entities.dtos.UsuarioDto;
import com.api.usafety_backend.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping("/criar")
    public ResponseEntity<TokenDto> criarUsuario(@RequestBody UsuarioDto usuarioDto) {
        log.info("POST /usuario/criar");
        log.info("Criando usuário " + usuarioDto.getUsername());

        TokenDto token = usuarioService.criar(new Usuario(usuarioDto));

        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarUsuario(
            @PathVariable("id") Long id) {
        log.info("GET /usuario/buscar");

        UsuarioDto usuarioDto = new UsuarioDto(usuarioService.buscarPorId(id));

        return ResponseEntity.ok(usuarioDto);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizarUsuario(
            Principal principal,
            @RequestBody UsuarioDto usuarioDto) {
        log.info("POST /usuario/atualizar");

        try {
            Usuario editor = usuarioService.buscarPorUsername(principal.getName());

            usuarioService.atualizar(new Usuario(usuarioDto), editor);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao atualizar usuário", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listarUsuarios")
    public ResponseEntity<List<UsuarioDto>> listarUsuarios(Principal principal) {
        log.info("GET /usuario/listarUsuarios");

        log.info("Usuário logado: " + principal.getName());

        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());

        if (!usuario.hasCargo(Usuario.Cargos.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(usuarioService.buscarTodosUsuariosDto());
    }
}
