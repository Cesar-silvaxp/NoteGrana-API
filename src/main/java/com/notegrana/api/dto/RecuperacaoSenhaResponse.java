package com.notegrana.api.dto;

public class RecuperacaoSenhaResponse {

    private final String mensagem;
    private final String tokenTeste;

    public RecuperacaoSenhaResponse(
        String mensagem,
        String tokenTeste
    ) {
        this.mensagem = mensagem;
        this.tokenTeste = tokenTeste;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getTokenTeste() {
        return tokenTeste;
    }
}