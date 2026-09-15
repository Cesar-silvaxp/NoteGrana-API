package com.notegrana.api.exception;

public class TokenRecuperacaoExpiradoException
    extends RuntimeException {

    public TokenRecuperacaoExpiradoException() {
        super(
            "O token de recuperação expirou."
        );
    }
}