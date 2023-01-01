package com.api.usafety_backend.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.api.usafety_backend.entities.dtos.ComentarioFullDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private LocalDateTime ultimaEdicao;

    @Column(columnDefinition = "boolean default true")
    private boolean visivel = true;

    @Column(columnDefinition = "boolean default false")
    private boolean editado = true;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private Usuario autor;

    @ManyToOne()
    @JoinColumn(name = "id_topico")
    private Topico topico;

    public Comentario(ComentarioFullDto dto) {
        this.id = dto.getId();
        this.conteudo = dto.getConteudo();
        this.horaPublicacao = dto.getHoraPublicacao();
        this.visivel = dto.isVisivel();
        this.editado = dto.isEditado();
        this.autor = new Usuario(dto.getAutor());
        this.topico = new Topico(dto.getTopico());
    }
}
