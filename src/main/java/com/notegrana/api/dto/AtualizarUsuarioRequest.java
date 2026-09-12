package com.notegrana.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AtualizarUsuarioRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(
        min = 2,
        max = 120,
        message = "O nome deve ter entre 2 e 120 caracteres."
    )
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(
        max = 180,
        message = "O e-mail deve ter no máximo 180 caracteres."
    )
    private String email;

    public AtualizarUsuarioRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}