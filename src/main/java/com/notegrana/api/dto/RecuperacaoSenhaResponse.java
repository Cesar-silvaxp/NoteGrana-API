package com.notegrana.api.dto;

public class RecuperacaoSenhaResponse {

    private final String mensagem;

    public RecuperacaoSenhaResponse(
        String mensagem
    ) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}