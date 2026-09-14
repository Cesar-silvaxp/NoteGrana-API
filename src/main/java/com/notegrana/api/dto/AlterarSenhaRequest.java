package com.notegrana.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlterarSenhaRequest {

    @NotBlank(
        message = "A senha atual é obrigatória."
    )
    private String senhaAtual;

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
        message = "A confirmação da nova senha é obrigatória."
    )
    private String confirmacaoNovaSenha;

    public AlterarSenhaRequest() {
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(
        String senhaAtual
    ) {
        this.senhaAtual = senhaAtual;
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