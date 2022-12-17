package com.api.usafety_backend.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.UsuarioDto;
import com.api.usafety_backend.services.AdminService;
import com.api.usafety_backend.services.UsuarioService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AdminService adminService;

    Logger log = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/criar")
    public ResponseEntity<Void> criarUsuario(
            Principal principal,
            @RequestBody UsuarioDto dto) {
        log.info("POST /admin/criar");

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.buscarPorUsername(principal.getName());

        if (usuario.isAdmin()) {
            usuarioService.criar(new Usuario(dto));

            return ResponseEntity.ok().build();
        } else {
            log.warn("Acesso negado ao tentar criar outro usuario para: " + usuario.getUsername());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
