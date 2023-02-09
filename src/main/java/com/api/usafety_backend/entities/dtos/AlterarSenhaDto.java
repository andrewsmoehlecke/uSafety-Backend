package com.api.usafety_backend.entities.dtos;

import lombok.Data;

@Data
public class AlterarSenhaDto {
    private String senhaAtual;
    private String novaSenha;
}
