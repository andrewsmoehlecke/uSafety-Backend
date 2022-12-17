package com.api.usafety_backend.entities.dtos;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ComentarioDto {
    private Long id;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private boolean visivel = true;

    private UsuarioDto autor;

    private TopicoFullDto topico;
}
