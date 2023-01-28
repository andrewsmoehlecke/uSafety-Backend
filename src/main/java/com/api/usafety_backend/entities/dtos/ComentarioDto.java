package com.api.usafety_backend.entities.dtos;

import java.time.LocalDateTime;

import com.api.usafety_backend.entities.Comentario;

import lombok.Data;

@Data
public class ComentarioDto {
    private Long id;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private LocalDateTime ultimaEdicao;

    private boolean visivel;

    private UsuarioDto autor;

    public ComentarioDto(Comentario c) {
        this.id = c.getId();
        this.conteudo = c.getConteudo();
        this.horaPublicacao = c.getHoraPublicacao();
        this.ultimaEdicao = c.getUltimaEdicao();
        this.visivel = c.isVisivel();
        this.autor = new UsuarioDto(c.getAutor());
    }
}
