package com.api.usafety_backend.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/listarUsuarios")
    public ResponseEntity<String> listarUsuarios(Principal principal) {
        log.info("GET /usuario/listarUsuarios");

        return ResponseEntity.ok("Teste");
    }
}
