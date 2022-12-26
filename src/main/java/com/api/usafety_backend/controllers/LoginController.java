package com.api.usafety_backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.entities.dtos.ErroDto;
import com.api.usafety_backend.entities.dtos.LoginUsuarioDto;
import com.api.usafety_backend.entities.dtos.TokenDto;
import com.api.usafety_backend.exceptions.ErroAoAutenticarUsuarioException;
import com.api.usafety_backend.exceptions.UsuarioDesabilitadoException;
import com.api.usafety_backend.exceptions.UsuarioNaoEncontradoException;
import com.api.usafety_backend.services.UsuarioService;
import com.api.usafety_backend.util.Constantes;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    private final Constantes constantes = new Constantes();

    Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping
    public ResponseEntity createAuthenticationToken(@RequestBody LoginUsuarioDto usuario) {
        log.info("POST /login");
        log.info("Usuario: " + usuario.getUsername());

        try {
            TokenDto dto = usuarioService.criarTokenParaUsuario(usuario);

            return ResponseEntity.ok(dto);
        } catch (UsuarioNaoEncontradoException e) {
            log.info("Usuario " + usuario.getUsername() + " não encontrado.");

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErroDto(constantes.USUARIO_NAO_ENCONTRADO));
        } catch (UsuarioDesabilitadoException e) {
            log.info("Usuario " + usuario.getUsername() + " está desabilitado.");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErroDto(constantes.USUARIO_DESABILITADO));
        } catch (ErroAoAutenticarUsuarioException e) {
            log.info("Erro ao autenticar usuario " + usuario.getUsername() + ".");

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErroDto(constantes.SENHA_INCORRETA));
        }
    }

    @PostMapping("/recuperarAcesso")
    public ResponseEntity<Void> recuperarAcesso(@RequestParam("username") String username) {
        log.info("POST /login/recuperarAcesso");
        log.info("Usuario: " + username);

        usuarioService.gerarCodigoDeRecuperacao(username);

        return ResponseEntity.ok().build();
    }
}
