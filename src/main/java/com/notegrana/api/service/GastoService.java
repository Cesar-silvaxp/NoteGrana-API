package com.notegrana.api.service;

import com.notegrana.api.dto.CriarGastoRequest;
import com.notegrana.api.dto.GastoResponse;
import com.notegrana.api.exception.GastoNaoEncontradoException;
import com.notegrana.api.model.Gasto;
import com.notegrana.api.model.StatusGasto;
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
        String idOrigem =
            limparTexto(
                request.getIdOrigem()
            );

        /*
         * Se o Android já enviou esse
         * gasto anteriormente, não cria
         * uma segunda linha.
         */
        if (idOrigem != null) {
            Gasto existente =
                gastoRepository
                    .findByUsuarioIdAndIdOrigem(
                        usuario.getId(),
                        idOrigem
                    )
                    .orElse(null);

            if (existente != null) {
                return converterParaResponse(
                    existente
                );
            }
        }

        String titulo =
            request
                .getTitulo()
                .trim();

        String descricao =
            limparTexto(
                request.getDescricao()
            );

        String pacoteOrigem =
            limparTexto(
                request.getPacoteOrigem()
            );

        Gasto gasto =
            new Gasto(
                request.getValor(),
                titulo,
                descricao,
                request.getDataHora(),
                idOrigem,
                pacoteOrigem,
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
            buscarEntidadeDoUsuario(
                gastoId,
                usuarioId
            );

        return converterParaResponse(
            gasto
        );
    }

    @Transactional
    public GastoResponse ignorarGasto(
        Long gastoId,
        Long usuarioId
    ) {
        return alterarStatus(
            gastoId,
            usuarioId,
            StatusGasto.IGNORADO
        );
    }

    @Transactional
    public GastoResponse reativarGasto(
        Long gastoId,
        Long usuarioId
    ) {
        return alterarStatus(
            gastoId,
            usuarioId,
            StatusGasto.ATIVO
        );
    }

    private GastoResponse alterarStatus(
        Long gastoId,
        Long usuarioId,
        StatusGasto status
    ) {
        Gasto gasto =
            buscarEntidadeDoUsuario(
                gastoId,
                usuarioId
            );

        gasto.setStatus(status);

        Gasto gastoAtualizado =
            gastoRepository.save(gasto);

        return converterParaResponse(
            gastoAtualizado
        );
    }

    private Gasto buscarEntidadeDoUsuario(
        Long gastoId,
        Long usuarioId
    ) {
        return gastoRepository
            .findByIdAndUsuarioId(
                gastoId,
                usuarioId
            )
            .orElseThrow(
                GastoNaoEncontradoException::new
            );
    }

    private String limparTexto(
        String texto
    ) {
        if (texto == null) {
            return null;
        }

        String limpo =
            texto.trim();

        if (limpo.isBlank()) {
            return null;
        }

        return limpo;
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
            gasto.getStatus(),
            gasto.getIdOrigem(),
            gasto.getPacoteOrigem()
        );
    }
}