package com.api.usafety_backend.entities.dtos;

import java.time.LocalDateTime;

import com.api.usafety_backend.entities.Comentario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CriarComentarioDto {
    private Long id;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private LocalDateTime ultimaEdicao;

    private boolean visivel;

    private boolean editado;

    private Long autor;

    private Long topico;

    public CriarComentarioDto(Comentario c) {
        this.id = c.getId();
        this.conteudo = c.getConteudo();
        this.horaPublicacao = c.getHoraPublicacao();
        this.ultimaEdicao = c.getUltimaEdicao();
        this.visivel = c.isVisivel();
        this.editado = c.isEditado();
        this.autor = c.getAutor().getId();
        this.topico = c.getTopico().getId();
    }
}
