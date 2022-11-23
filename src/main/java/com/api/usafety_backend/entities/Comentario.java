package com.api.usafety_backend.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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

    @Column(columnDefinition = "boolean default true")
    private boolean visivel = true;

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private Usuario autor;

    @ManyToOne()
    @JoinColumn(name = "id_topico")
    private Topico topico;
}
