package com.notegrana.api.exception;

public class EmailRecuperacaoException
    extends RuntimeException {

    public EmailRecuperacaoException(
        String mensagem,
        Throwable causa
    ) {
        super(
            mensagem,
            causa
        );
    }
}