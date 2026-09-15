package com.notegrana.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gastos")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal valor;

    @Column(
        nullable = false,
        length = 120
    )
    private String titulo;

    @Column(
        length = 500
    )
    private String descricao;

    @Column(
        name = "data_hora",
        nullable = false
    )
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 20
    )
    private StatusGasto status;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "usuario_id",
        nullable = false
    )
    private Usuario usuario;

    public Gasto() {
    }

    public Gasto(
        BigDecimal valor,
        String titulo,
        String descricao,
        LocalDateTime dataHora,
        Usuario usuario
    ) {
        this.valor = valor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.usuario = usuario;
        this.status = StatusGasto.ATIVO;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(
        BigDecimal valor
    ) {
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(
        String titulo
    ) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(
        String descricao
    ) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(
        LocalDateTime dataHora
    ) {
        this.dataHora = dataHora;
    }

    public StatusGasto getStatus() {
        return status;
    }

    public void setStatus(
        StatusGasto status
    ) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(
        Usuario usuario
    ) {
        this.usuario = usuario;
    }
}