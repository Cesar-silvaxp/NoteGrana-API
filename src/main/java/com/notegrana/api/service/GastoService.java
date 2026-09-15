package com.notegrana.api.service;

import com.notegrana.api.dto.CriarGastoRequest;
import com.notegrana.api.dto.GastoResponse;
import com.notegrana.api.exception.GastoNaoEncontradoException;
import com.notegrana.api.model.Gasto;
import com.notegrana.api.model.Usuario;
import com.notegrana.api.repository.GastoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;

    public GastoService(
        GastoRepository gastoRepository
    ) {
        this.gastoRepository =
            gastoRepository;
    }

    @Transactional
    public GastoResponse criarGasto(
        CriarGastoRequest request,
        Usuario usuario
    ) {
        String titulo =
            request
                .getTitulo()
                .trim();

        String descricao =
            request.getDescricao();

        if (descricao != null) {
            descricao =
                descricao.trim();

            if (descricao.isBlank()) {
                descricao = null;
            }
        }

        Gasto gasto =
            new Gasto(
                request.getValor(),
                titulo,
                descricao,
                request.getDataHora(),
                usuario
            );

        Gasto gastoSalvo =
            gastoRepository.save(gasto);

        return converterParaResponse(
            gastoSalvo
        );
    }

    @Transactional(readOnly = true)
    public List<GastoResponse> listarGastos(
        Long usuarioId
    ) {
        return gastoRepository
            .findAllByUsuarioIdOrderByDataHoraDesc(
                usuarioId
            )
            .stream()
            .map(this::converterParaResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public GastoResponse buscarPorId(
        Long gastoId,
        Long usuarioId
    ) {
        Gasto gasto =
            gastoRepository
                .findByIdAndUsuarioId(
                    gastoId,
                    usuarioId
                )
                .orElseThrow(
                    GastoNaoEncontradoException::new
                );

        return converterParaResponse(
            gasto
        );
    }

    private GastoResponse converterParaResponse(
        Gasto gasto
    ) {
        return new GastoResponse(
            gasto.getId(),
            gasto.getValor(),
            gasto.getTitulo(),
            gasto.getDescricao(),
            gasto.getDataHora(),
            gasto.getStatus()
        );
    }
}