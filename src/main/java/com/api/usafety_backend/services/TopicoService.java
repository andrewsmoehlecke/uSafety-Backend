package com.api.usafety_backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Topico;
import com.api.usafety_backend.entities.Topico.Tipos;
import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.TopicoFullDto;
import com.api.usafety_backend.exceptions.UsuarioNaoAutorizadoException;
import com.api.usafety_backend.repositories.TopicoRepository;
import com.api.usafety_backend.util.Constantes;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    private ModelMapper mapper = new ModelMapper();

    private Constantes constantes = new Constantes();

    Logger log = LoggerFactory.getLogger(TopicoService.class);

    public void salvar(Topico topico) {
        topicoRepository.save(topico);
    }

    public void salvar(TopicoFullDto topicoDto, Topico.Tipos tipo) {
        topicoRepository.save(new Topico(topicoDto, tipo));
    }

    public void salvar(TopicoFullDto topicoDto, Topico.Tipos tipo, String username) {
        Usuario usuario = username != null ? usuarioService.buscarPorUsername(username) : new Usuario();

        if (usuario.isAdmin()) {
            topicoRepository.save(new Topico(topicoDto, tipo));
        } else {
            log.warn("Usuario " + username + " não autorizado a criar tópico de conteúdo");
            throw new UsuarioNaoAutorizadoException("Usuario não autorizado a criar tópico de conteúdo");
        }
    }

    public void excluir(Long idTopico, Usuario usuario) {
        log.info("Usuario " + usuario.getUsername() + " está deletando o tópico " + idTopico);

        Topico topico = topicoRepository.findById(idTopico).get();

        if (topico.getAutor() == usuario || usuario.isAdmin()) {
            topicoRepository.deleteById(idTopico);
        } else {
            log.warn("Usuario " + usuario.getUsername() + " não tem permissão para deletar o tópico "
                    + topico.getId());
        }
    }

    public Topico buscarPorId(Long id) {
        return topicoRepository.findById(id).get();
    }

    public List<TopicoFullDto> buscarTopicosPorTipo(Tipos tipo) {
        List<Topico> topicos = topicoRepository.findAllByTipoDeTopico(tipo);

        if (!topicos.isEmpty()) {
            return topicos.stream().map(topico -> mapper.map(topico, TopicoFullDto.class))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<TopicoFullDto>();
        }
    }

    public void editar(TopicoFullDto topicoDto, Usuario usuario) {
        log.info("Usuario " + usuario.getUsername() + " está editando o tópico " + topicoDto.getId());

        Topico topico = new Topico(topicoDto);

        topico.setHoraEdicao(LocalDateTime.now());

        if (topico.getAutor().getId() == usuario.getId() || usuario.isAdmin()) {
            topicoRepository.save(topico);
        } else {
            log.warn("Usuario " + usuario.getUsername() + " não tem permissão para editar o tópico "
                    + topicoDto.getId());
        }
    }
}
