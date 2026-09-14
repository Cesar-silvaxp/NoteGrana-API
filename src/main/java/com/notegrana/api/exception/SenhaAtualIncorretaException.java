package com.notegrana.api.exception;

public class SenhaAtualIncorretaException
    extends RuntimeException {

    public SenhaAtualIncorretaException() {
        super("A senha atual está incorreta.");
    }
}