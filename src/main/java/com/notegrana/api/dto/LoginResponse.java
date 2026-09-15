package com.notegrana.api.dto;

import java.time.Instant;

public class LoginResponse {

    private final String token;
    private final String tipo;
    private final Instant expiraEm;
    private final UsuarioResponse usuario;

    public LoginResponse(
        String token,
        String tipo,
        Instant expiraEm,
        UsuarioResponse usuario
    ) {
        this.token = token;
        this.tipo = tipo;
        this.expiraEm = expiraEm;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Instant getExpiraEm() {
        return expiraEm;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }
}