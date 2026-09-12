package com.notegrana.api.dto;

import java.time.LocalDateTime;

public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private LocalDateTime criadoEm;

    public UsuarioResponse(
        Long id,
        String nome,
        String email,
        LocalDateTime criadoEm
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}