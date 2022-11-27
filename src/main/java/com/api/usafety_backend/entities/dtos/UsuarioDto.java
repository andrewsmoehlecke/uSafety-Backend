package com.api.usafety_backend.entities.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.entities.Usuario.Cargos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class UsuarioDto extends LoginUsuarioDto {

    private Long id;

    private String nomeCompleto;

    private String email;

    private String fotoPerfil;

    private LocalDate dataNascimento;

    private LocalDateTime criacaoConta;

    private boolean ativo;

    private Set<Cargos> cargos = new HashSet<>();

    public UsuarioDto() {
        super();
    }

    public UsuarioDto(Usuario u) {
        super(u);

        this.id = u.getId();
        this.nomeCompleto = u.getNomeCompleto();
        this.email = u.getEmail();
        this.fotoPerfil = u.getFotoPerfil();
        this.dataNascimento = u.getDataNascimento();
        this.criacaoConta = u.getCriacaoConta();
        this.ativo = u.isAtivo();
        this.cargos = u.getCargos();
    }
}
