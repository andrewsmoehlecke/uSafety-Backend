package com.api.usafety_backend.entities.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class TokenDto {

    private String token;
    private boolean admin;
    private String username;

    public TokenDto(String token) {
        this.token = token;
    }
}
