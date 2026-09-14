package com.notegrana.api.exception;

public class SenhasNaoConferemException
    extends RuntimeException {

    public SenhasNaoConferemException() {
        super(
            "A nova senha e a confirmação não coincidem."
        );
    }
}