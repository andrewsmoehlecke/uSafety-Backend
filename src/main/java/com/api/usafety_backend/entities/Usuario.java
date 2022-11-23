package com.api.usafety_backend.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String nomeCompleto;

    private String email;

    private String senha;

    private String fotoPerfil;

    private LocalDate dataNascimento;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;

    @OneToMany(mappedBy = "autor")
    private List<Topico> topicos;

    @OneToMany(mappedBy = "autor")
    private List<Comentario> comentarios;

    private Cargo cargo;

    public enum Cargo {
        ADMIN("ADMIN"), USUARIO("USER");

        private final String cargo;

        Cargo(String cargo) {
            this.cargo = cargo;
        }

        public String getCargo() {
            return cargo;
        }
    }
}
