package com.api.usafety_backend.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.api.usafety_backend.entities.dtos.TopicoFullDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topico")
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime horaPublicacao;

    @Column
    private LocalDateTime horaEdicao;

    private String imagem;

    @Column(columnDefinition = "boolean default false")
    private boolean anonimo = false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario autor;

    @OneToMany(mappedBy = "topico")
    private List<Comentario> comentarios;

    @Fetch(FetchMode.SUBSELECT)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipos tipoDeTopico;

    public enum Tipos {
        DUVIDA("DUVIDA"), DISCUSSAO("DISCUSSAO"), CONTEUDO("CONTEUDO");

        private final String tipo;

        Tipos(String tipo) {
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }
    }

    public Topico(TopicoFullDto topicoDto) {
        this.id = topicoDto.getId();
        this.titulo = topicoDto.getTitulo();
        this.conteudo = topicoDto.getConteudo();
        this.horaPublicacao = topicoDto.getHoraPublicacao();
        this.imagem = topicoDto.getImagem();
        this.anonimo = topicoDto.isAnonimo();
        this.autor = new Usuario(topicoDto.getAutor());
    }

    public Topico(TopicoFullDto topicoDto, Tipos tipoDeTopico) {
        this.id = topicoDto.getId();
        this.titulo = topicoDto.getTitulo();
        this.conteudo = topicoDto.getConteudo();
        this.horaPublicacao = LocalDateTime.now();
        this.imagem = topicoDto.getImagem();
        this.anonimo = topicoDto.isAnonimo();
        this.autor = new Usuario(topicoDto.getAutor());
        this.tipoDeTopico = tipoDeTopico;
    }
}