package com.notegrana.api.controller;

import com.notegrana.api.dto.RecuperacaoSenhaResponse;
import com.notegrana.api.dto.RedefinirSenhaRequest;
import com.notegrana.api.dto.SolicitarRecuperacaoSenhaRequest;
import com.notegrana.api.service.RecuperacaoSenhaService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService service;

    public RecuperacaoSenhaController(
        RecuperacaoSenhaService service
    ) {
        this.service = service;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<RecuperacaoSenhaResponse>
        solicitar(
            @Valid
            @RequestBody
            SolicitarRecuperacaoSenhaRequest request
        ) {

        return ResponseEntity.ok(
            service.solicitarRecuperacao(
                request
            )
        );
    }

    @PostMapping("/redefinir")
    public ResponseEntity<Void>
        redefinir(
            @Valid
            @RequestBody
            RedefinirSenhaRequest request
        ) {

        service.redefinirSenha(
            request
        );

        return ResponseEntity
            .noContent()
            .build();
    }
}