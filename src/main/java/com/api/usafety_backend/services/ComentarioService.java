package com.api.usafety_backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Comentario;
import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.dtos.ComentarioFullDto;
import com.api.usafety_backend.entities.dtos.CriarComentarioDto;
import com.api.usafety_backend.repositories.ComentarioRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TopicoService topicoService;

    Logger log = LoggerFactory.getLogger(ComentarioService.class);

    public void criar(CriarComentarioDto dto, Usuario autor) {
        log.info(autor.getUsername() + " esta comentanto no topico: " + dto.getTopico());

        try {

            if (dto.getAutor().equals(autor.getId())) {
                Comentario comentario = new Comentario();

                comentario.setConteudo(dto.getConteudo());
                comentario.setHoraPublicacao(LocalDateTime.now());
                comentario.setVisivel(true);
                comentario.setAutor(usuarioService.buscarPorId(dto.getAutor()));
                comentario.setTopico(topicoService.buscarPorId(dto.getTopico()));

                comentarioRepository.save(comentario);
            } else {
                log.warn("Usuario nao autorizado a comentar no topico");
            }
        } catch (Exception e) {
            log.error("Erro ao criar comentario: ", e);
        }
    }

    public void editar(Long id, String conteudo, Usuario autor) {
        log.info(autor.getUsername() + " esta editando o comentario: " + id);

        try {
            Comentario comentario = comentarioRepository.findById(id).get();

            if (comentario.getAutor().equals(autor)) {
                comentario.setConteudo(conteudo);
                comentario.setEditado(true);
                comentario.setUltimaEdicao(LocalDateTime.now());

                comentarioRepository.save(comentario);
            } else {
                log.warn("Usuario nao autorizado a editar o comentario");
            }
        } catch (Exception e) {
            log.error("Erro ao editar comentario: ", e);
        }
    }

    public void deletar(Long id, Usuario autor) {
        log.info(autor.getUsername() + " esta deletando o comentario: " + id);

        try {

            Comentario comentario = comentarioRepository.findById(id).get();

            if (autor.isAdmin() || comentario.getAutor().getId().equals(autor.getId())) {
                comentario.setVisivel(false);
                comentarioRepository.save(comentario);
            } else {
                log.warn("Usuario nao autorizado a deletar o comentario");
            }
        } catch (Exception e) {
            log.error("Erro ao deletar comentario: ", e);
        }
    }

    public List<ComentarioFullDto> buscarPorTopico(Long id) {
        log.info("Buscando comentarios do topico: " + id);
        try {
            return comentarioRepository.findByTopico_Id(id)
                    .stream()
                    .map(ComentarioFullDto::new)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao buscar comentarios: ", e);
            return new ArrayList<ComentarioFullDto>();
        }
    }
}
