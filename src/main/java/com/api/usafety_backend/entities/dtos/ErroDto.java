package com.api.usafety_backend.entities.dtos;

public class ErroDto {
    private String error;

    public ErroDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
