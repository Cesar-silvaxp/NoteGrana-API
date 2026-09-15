package com.notegrana.api.exception;

public class GastoNaoEncontradoException
    extends RuntimeException {

    public GastoNaoEncontradoException() {
        super("Gasto não encontrado.");
    }
}