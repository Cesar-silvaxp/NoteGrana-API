package com.notegrana.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErroResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String mensagem;
    private final Map<String, String> campos;

    public ErroResponse(
        int status,
        String mensagem,
        Map<String, String> campos
    ) {
        this.timestamp =
            LocalDateTime.now();

        this.status = status;
        this.mensagem = mensagem;
        this.campos = campos;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}