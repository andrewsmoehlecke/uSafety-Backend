package com.api.usafety_backend.services;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.configs.CustomUserDetailsService;
import com.api.usafety_backend.configs.TokenHandler;
import com.api.usafety_backend.configs.UserPrincipal;
import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.LoginUsuarioDto;
import com.api.usafety_backend.entities.dtos.TokenDto;
import com.api.usafety_backend.exceptions.CamposObrigatoriosNaoPreenchidosException;
import com.api.usafety_backend.exceptions.EmailInvalidoException;
import com.api.usafety_backend.exceptions.ErroAoAutenticarUsuarioException;
import com.api.usafety_backend.exceptions.UsuarioDesabilitadoException;
import com.api.usafety_backend.exceptions.UsuarioJaExistenteException;
import com.api.usafety_backend.repositories.UsuarioRepository;
import com.api.usafety_backend.util.Constantes;
import com.api.usafety_backend.util.RegexValidador;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHandler tokenHandler;

    private final Constantes constantes = new Constantes();

    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private BCryptPasswordEncoder codificadorDeSenha() {
        return new BCryptPasswordEncoder();
    }

    public TokenDto criar(Usuario u) {
        String senhaUsuario = u.getSenha();

        try {
            if (validarUsuario(u)) {

                if (u.getCargos().isEmpty() || u.getCargos() == null) {
                    u.addCargo(Usuario.Cargos.USUARIO);
                }

                if (u.getUsername().equals(constantes.ADMIN_USERNAME)) {
                    u.addCargo(Usuario.Cargos.USUARIO);
                    u.addCargo(Usuario.Cargos.ADMIN);
                }

                u.setAtivo(true);
                u.setSenha(codificadorDeSenha().encode(u.getSenha()));

                LoginUsuarioDto loginUsuario = new LoginUsuarioDto(usuarioRepository.save(u));
                loginUsuario.setSenha(senhaUsuario);

                return criarTokenParaUsuario(loginUsuario);
            }

            return null;
        } catch (Exception e) {
            log.error("Erro ao criar o usuario " + u.getUsername(), e);

            throw new ErroAoAutenticarUsuarioException("Erro ao criar o usuario " + u.getUsername());
        }
    }

    public void atualizar(Usuario u) {
        if (validarUsuario(u)) {
            usuarioRepository.save(u);
        }
    }

    /*
     * Validando os campos do usuário
     */
    public boolean validarUsuario(Usuario u) {
        try {
            log.info("Validando o usuario " + u.getUsername());

            if (usuarioRepository.findByUsername(u.getUsername()) != null) {
                throw new UsuarioJaExistenteException("Usuario já existente.");
            }
            if (!RegexValidador.validador(u.getEmail(), constantes.REGEX_EMAIL)) {
                throw new EmailInvalidoException("Email invalido.");
            }
            if (!RegexValidador.validador(u.getUsername(), constantes.REGEX_USERNAME)) {
                throw new EmailInvalidoException("Username invalido.");
            }
            if (!RegexValidador.validador(u.getFotoPerfil(), constantes.REGEX_IMAGEM_URL)) {
                throw new EmailInvalidoException("URL da imagem invalida.");
            }
            if (u.getNomeCompleto() == null || u.getNomeCompleto().isEmpty()) {
                throw new CamposObrigatoriosNaoPreenchidosException("Nome não pode ser nulo ou vazio.");
            }
            if (u.getDataNascimento() == null) {
                throw new CamposObrigatoriosNaoPreenchidosException("Data de nascimento não pode ser nula.");
            }

            return true;
        } catch (

        NullPointerException e) {
            log.error("Usuário não possui todos os campos obrigatórios preenchidos.", e);

            throw new CamposObrigatoriosNaoPreenchidosException(
                    "Usuário não possui todos os campos obrigatórios preenchidos.");
        }
    }

    public Usuario buscarPorUsername(String username) {
        log.debug(usuarioRepository.findByUsername(username).toString());
        return usuarioRepository.findByUsername(username);
    }

    /*
     * Criando um token JWT para um usuário
     */
    public TokenDto criarTokenParaUsuario(LoginUsuarioDto usuario) {
        log.info("Criando token para o usuario " + usuario.getUsername());

        UserPrincipal principal = null;

        principal = (UserPrincipal) customUserDetailService
                .loadUserByUsername(usuario.getUsername());

        if (!principal.isEnabled()) {
            log.info("Usuario " + usuario.getUsername() + " está desabilitado.");

            throw new UsuarioDesabilitadoException(constantes.USUARIO_DESABILITADO);
        } else {
            try {
                autenticar(usuario.getUsername(), usuario.getSenha());

                log.info("Usuario com credenciais válidas.");

                String token = tokenHandler.createTokenForUser(principal);

                TokenDto tokenDto = new TokenDto(token);

                return tokenDto;
            } catch (UsuarioDesabilitadoException e) {
                log.error("Erro ao autenticar usuario ", e);

                throw new ErroAoAutenticarUsuarioException(constantes.SENHA_INCORRETA);
            }
        }
    }

    /*
     * Verificando autenticação do usuário
     */
    private void autenticar(String username, String senha) throws UsuarioDesabilitadoException {
        log.info("Autenticando usuário " + username);

        Objects.requireNonNull(username, "O usuario esta nulo.");
        Objects.requireNonNull(senha, "A senha esta nula.");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, senha));
        } catch (DisabledException e) {
            log.error("Usuario desabilitado. Username: " + username + "; Login " + senha, e);
            throw new UsuarioDesabilitadoException("Usuario desabilitado. Username: " + username + "; Login " + senha);
        } catch (LockedException e) {
            log.error("Usuario bloqueado. Username: " + username + "; Senha " + senha, e);
            throw new UsuarioDesabilitadoException("Usuario bloqueado. Username: " + username + "; Senha " + senha);
        } catch (BadCredentialsException e) {
            log.error("Credednciais invalidas. Username: " + username + "; Senha " + senha, e);
            throw new UsuarioDesabilitadoException(
                    "Credednciais invalidas. Username: " + username + "; Senha " + senha);
        }
    }
}
