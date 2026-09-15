package com.notegrana.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaRequest {

    @NotBlank(
        message = "O token é obrigatório."
    )
    private String token;

    @NotBlank(
        message = "A nova senha é obrigatória."
    )
    @Size(
        min = 6,
        max = 100,
        message = "A nova senha deve ter entre 6 e 100 caracteres."
    )
    private String novaSenha;

    @NotBlank(
        message = "A confirmação da senha é obrigatória."
    )
    private String confirmacaoNovaSenha;

    public RedefinirSenhaRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(
        String novaSenha
    ) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoNovaSenha() {
        return confirmacaoNovaSenha;
    }

    public void setConfirmacaoNovaSenha(
        String confirmacaoNovaSenha
    ) {
        this.confirmacaoNovaSenha =
            confirmacaoNovaSenha;
    }
}