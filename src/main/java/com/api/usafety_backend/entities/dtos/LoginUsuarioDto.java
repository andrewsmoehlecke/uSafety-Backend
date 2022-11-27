package com.api.usafety_backend.entities.dtos;

import com.api.usafety_backend.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginUsuarioDto {

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    public LoginUsuarioDto() {
    }

    public LoginUsuarioDto(Usuario u) {
        this.username = u.getUsername();
        this.senha = u.getSenha();
    }
}
