package com.api.usafety_backend.entities.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RespostaSimplesDto {
    private String resposta;

    public RespostaSimplesDto(String resposta) {
        this.resposta = resposta;
    }
}
