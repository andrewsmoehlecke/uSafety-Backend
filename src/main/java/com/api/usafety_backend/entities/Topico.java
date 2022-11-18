package com.api.usafety_backend.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

    private String titulo;

    private String conteudo;

    private LocalDateTime horaPublicacao;

    private String imagem;

    @Column(columnDefinition = "boolean default false")
    private boolean anonimo = false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario autor;

    @OneToMany(mappedBy = "topico")
    private List<Comentario> comentarios;
}
