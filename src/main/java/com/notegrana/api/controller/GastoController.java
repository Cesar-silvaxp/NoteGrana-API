package com.notegrana.api.controller;

import com.notegrana.api.dto.CriarGastoRequest;
import com.notegrana.api.dto.GastoResponse;
import com.notegrana.api.model.Usuario;
import com.notegrana.api.service.GastoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(
        GastoService gastoService
    ) {
        this.gastoService =
            gastoService;
    }

    @PostMapping
    public ResponseEntity<GastoResponse>
        criarGasto(
            @AuthenticationPrincipal
            Usuario usuario,

            @Valid
            @RequestBody
            CriarGastoRequest request
        ) {

        GastoResponse gasto =
            gastoService.criarGasto(
                request,
                usuario
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(gasto);
    }

    @GetMapping
    public ResponseEntity<
        List<GastoResponse>
    > listarGastos(
        @AuthenticationPrincipal
        Usuario usuario
    ) {

        return ResponseEntity.ok(
            gastoService.listarGastos(
                usuario.getId()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponse>
        buscarGasto(
            @PathVariable Long id,

            @AuthenticationPrincipal
            Usuario usuario
        ) {

        return ResponseEntity.ok(
            gastoService.buscarPorId(
                id,
                usuario.getId()
            )
        );
    }
}