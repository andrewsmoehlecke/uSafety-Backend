package com.api.usafety_backend.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Enumerated(EnumType.STRING)
    private Set<Cargos> cargos = new HashSet<>();

    public enum Cargos {
        ADMIN("ADMIN"), USUARIO("USER");

        private final String cargo;

        Cargos(String cargo) {
            this.cargo = cargo;
        }

        public String getCargo() {
            return cargo;
        }
    }

    public List<String> getCargosString() {
        return cargos.stream().map(Cargos::toString).collect(Collectors.toList());
    }

    public boolean hasCargo(Cargos c) {
        return cargos.contains(c);
    }

    public void addCargo(Cargos c) {
        cargos.add(c);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return cargos.stream().map(cargo -> {
            return new SimpleGrantedAuthority("ROLE_".concat(cargo.getCargo()));
        }).collect(Collectors.toList());
    }

}
