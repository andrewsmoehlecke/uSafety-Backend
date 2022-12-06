package com.api.usafety_backend.controllers;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.usafety_backend.configs.CustomUserDetailsService;
import com.api.usafety_backend.configs.TokenHandler;
import com.api.usafety_backend.configs.UserPrincipal;
import com.api.usafety_backend.entities.dtos.ErroDto;
import com.api.usafety_backend.entities.dtos.LoginUsuarioDto;
import com.api.usafety_backend.entities.dtos.TokenDto;
import com.api.usafety_backend.exceptions.UsuarioNaoEncontradoException;
import com.api.usafety_backend.exceptions.UsuarioNaoHabilitadoException;
import com.api.usafety_backend.util.Constantes;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private CustomUserDetailsService customUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHandler tokenHandler;

    private final Constantes constantes = new Constantes();

    Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping
    public ResponseEntity createAuthenticationToken(@RequestBody LoginUsuarioDto usuario)
            throws Exception {
        log.info("POST /login");
        log.info("Usuario: " + usuario.getUsername());

        UserPrincipal principal = null;

        try {
            principal = (UserPrincipal) customUserDetailService
                    .loadUserByUsername(usuario.getUsername());
        } catch (UsuarioNaoEncontradoException e) {
            log.info("Usuario " + usuario.getUsername() + " não encontrado.");

            return new ResponseEntity<>(new ErroDto(constantes.USUARIO_NAO_ENCONTRADO), HttpStatus.OK);
        }

        if (!principal.isEnabled()) {
            log.info("Usuario " + usuario.getUsername() + " está desabilitado.");

            return new ResponseEntity<>(new ErroDto(constantes.USUARIO_DESABILITADO), HttpStatus.OK);
        } else {
            try {
                autenticar(usuario.getUsername(), usuario.getSenha());

                log.info("Usuario com credenciais válidas.");

                String token = tokenHandler.createTokenForUser(principal);

                TokenDto tokenDto = new TokenDto(token);

                return new ResponseEntity<>(tokenDto, HttpStatus.OK);
            } catch (UsuarioNaoHabilitadoException e) {
                log.error("Erro ao autenticar usuario ", e);

                ErroDto erro = new ErroDto(constantes.SENHA_INCORRETA);
                return new ResponseEntity<>(erro, HttpStatus.OK);
            }
        }
    }

    private void autenticar(String username, String senha) throws UsuarioNaoHabilitadoException {
        log.info("Autenticando usuário " + username);

        Objects.requireNonNull(username, "O usuario esta nulo.");
        Objects.requireNonNull(senha, "A senha esta nula.");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, senha));
        } catch (DisabledException e) {
            log.error("Usuario desabilitado. Username: " + username + "; Login " + senha, e);
            throw new UsuarioNaoHabilitadoException("Usuario desabilitado. Username: " + username + "; Login " + senha);
        } catch (LockedException e) {
            log.error("Usuario bloqueado. Username: " + username + "; Senha " + senha, e);
            throw new UsuarioNaoHabilitadoException("Usuario bloqueado. Username: " + username + "; Senha " + senha);
        } catch (BadCredentialsException e) {
            log.error("Credednciais invalidas. Username: " + username + "; Senha " + senha, e);
            throw new UsuarioNaoHabilitadoException(
                    "Credednciais invalidas. Username: " + username + "; Senha " + senha);
        }
    }

}
