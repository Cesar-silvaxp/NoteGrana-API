package com.notegrana.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SolicitarRecuperacaoSenhaRequest {

    @NotBlank(
        message = "O e-mail é obrigatório."
    )
    @Email(
        message = "Informe um e-mail válido."
    )
    private String email;

    public SolicitarRecuperacaoSenhaRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}