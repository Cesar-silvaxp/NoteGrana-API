package com.notegrana.api.exception;

public class TokenRecuperacaoInvalidoException
    extends RuntimeException {

    public TokenRecuperacaoInvalidoException() {
        super(
            "O token de recuperação é inválido."
        );
    }
}