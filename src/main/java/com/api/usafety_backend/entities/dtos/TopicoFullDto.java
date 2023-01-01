package com.api.usafety_backend.entities.dtos;

import java.time.LocalDateTime;

import com.api.usafety_backend.entities.Topico;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class TopicoFullDto {
    private Long id;

    private String titulo;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private LocalDateTime horaEdicao;

    private String imagem;

    private boolean anonimo;

    private UsuarioDto autor;

    public TopicoFullDto(Topico dto) {
        this.id = dto.getId();
        this.titulo = dto.getTitulo();
        this.conteudo = dto.getConteudo();
        this.horaPublicacao = dto.getHoraPublicacao();
        this.imagem = dto.getImagem();
        this.anonimo = dto.isAnonimo();
        this.autor = new UsuarioDto(dto.getAutor());
    }
}
