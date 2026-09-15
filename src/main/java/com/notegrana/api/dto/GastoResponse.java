package com.notegrana.api.dto;

import com.notegrana.api.model.StatusGasto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GastoResponse {

    private final Long id;
    private final BigDecimal valor;
    private final String titulo;
    private final String descricao;
    private final LocalDateTime dataHora;
    private final StatusGasto status;

    public GastoResponse(
        Long id,
        BigDecimal valor,
        String titulo,
        String descricao,
        LocalDateTime dataHora,
        StatusGasto status
    ) {
        this.id = id;
        this.valor = valor;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusGasto getStatus() {
        return status;
    }
}