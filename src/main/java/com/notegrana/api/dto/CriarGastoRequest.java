package com.notegrana.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CriarGastoRequest {

    @NotNull(
        message = "O valor é obrigatório."
    )
    @DecimalMin(
        value = "0.01",
        message = "O valor deve ser maior que zero."
    )
    @Digits(
        integer = 10,
        fraction = 2,
        message = "O valor deve possuir no máximo 2 casas decimais."
    )
    private BigDecimal valor;

    @NotBlank(
        message = "O título é obrigatório."
    )
    @Size(
        max = 120,
        message = "O título deve possuir no máximo 120 caracteres."
    )
    private String titulo;

    @Size(
        max = 500,
        message = "A descrição deve possuir no máximo 500 caracteres."
    )
    private String descricao;

    @NotNull(
        message = "A data e hora são obrigatórias."
    )
    private LocalDateTime dataHora;

    @Size(
        max = 100,
        message = "O identificador de origem deve possuir no máximo 100 caracteres."
    )
    private String idOrigem;

    @Size(
        max = 180,
        message = "O pacote de origem deve possuir no máximo 180 caracteres."
    )
    private String pacoteOrigem;

    public CriarGastoRequest() {
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

    public String getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(
        String idOrigem
    ) {
        this.idOrigem = idOrigem;
    }

    public String getPacoteOrigem() {
        return pacoteOrigem;
    }

    public void setPacoteOrigem(
        String pacoteOrigem
    ) {
        this.pacoteOrigem = pacoteOrigem;
    }
}