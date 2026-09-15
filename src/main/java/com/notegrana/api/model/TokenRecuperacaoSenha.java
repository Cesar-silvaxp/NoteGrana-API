package com.notegrana.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_recuperacao_senha")
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        unique = true,
        length = 100
    )
    private String token;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "usuario_id",
        nullable = false
    )
    private Usuario usuario;

    @Column(
        name = "criado_em",
        nullable = false,
        updatable = false
    )
    private LocalDateTime criadoEm;

    @Column(
        name = "expira_em",
        nullable = false
    )
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean usado = false;

    public TokenRecuperacaoSenha() {
    }

    public TokenRecuperacaoSenha(
        String token,
        Usuario usuario,
        LocalDateTime expiraEm
    ) {
        this.token = token;
        this.usuario = usuario;
        this.expiraEm = expiraEm;
    }

    @PrePersist
    public void antesDeSalvar() {
        criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public boolean estaExpirado() {
        return LocalDateTime.now()
            .isAfter(expiraEm);
    }
}