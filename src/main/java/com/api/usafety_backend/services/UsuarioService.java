package com.api.usafety_backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.exceptions.CamposObrigatoriosNaoPreenchidosException;
import com.api.usafety_backend.exceptions.EmailInvalidoException;
import com.api.usafety_backend.exceptions.UsuarioJaExistenteException;
import com.api.usafety_backend.repositories.UsuarioRepository;
import com.api.usafety_backend.util.Constantes;
import com.api.usafety_backend.util.RegexValidador;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private BCryptPasswordEncoder codificadorDeSenha() {
        return new BCryptPasswordEncoder();
    }

    public void salvar(Usuario u) {
        if (validarUsuario(u)) {

            if (u.getCargos().isEmpty() || u.getCargos() == null) {
                u.addCargo(Usuario.Cargos.USUARIO);
            }
            if (u.getUsername().equals(Constantes.ADMIN_USERNAME)) {
                u.addCargo(Usuario.Cargos.USUARIO);
                u.addCargo(Usuario.Cargos.ADMIN);
            }

            u.setSenha(codificadorDeSenha().encode(u.getSenha()));

            usuarioRepository.save(u);
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
            if (!RegexValidador.validador(u.getEmail(), Constantes.REGEX_EMAIL)) {
                throw new EmailInvalidoException("Email invalido.");
            }
            if (!RegexValidador.validador(u.getUsername(), Constantes.REGEX_USERNAME)) {
                throw new EmailInvalidoException("Username invalido.");
            }
            if (!RegexValidador.validador(u.getFotoPerfil(), Constantes.REGEX_IMAGEM_URL)) {
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
}
