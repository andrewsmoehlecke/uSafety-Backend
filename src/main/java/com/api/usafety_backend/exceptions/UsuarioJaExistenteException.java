package com.api.usafety_backend.exceptions;

public class UsuarioJaExistenteException extends RuntimeException {

    public UsuarioJaExistenteException(String mensagem) {
        super(mensagem);
    }

}
