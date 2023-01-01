package com.api.usafety_backend.exceptions;

public class UsuarioNaoAutorizadoException extends RuntimeException {

    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }
}
