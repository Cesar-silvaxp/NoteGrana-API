package com.notegrana.api.controller;

import com.notegrana.api.dto.AtualizarUsuarioRequest;
import com.notegrana.api.dto.CriarUsuarioRequest;
import com.notegrana.api.dto.LoginRequest;
import com.notegrana.api.dto.UsuarioResponse;
import com.notegrana.api.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
        UsuarioService usuarioService
    ) {
        this.usuarioService =
            usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse>
        criarUsuario(
            @Valid
            @RequestBody
            CriarUsuarioRequest request
        ) {

        UsuarioResponse usuario =
            usuarioService.criarUsuario(
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuario);
    }

    @GetMapping
    public ResponseEntity<
        List<UsuarioResponse>
    > listarUsuarios() {

        return ResponseEntity.ok(
            usuarioService
                .listarUsuarios()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse>
        buscarUsuario(
            @PathVariable Long id
        ) {

        return ResponseEntity.ok(
            usuarioService
                .buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse>
        atualizarUsuario(
            @PathVariable Long id,
            @Valid
            @RequestBody
            AtualizarUsuarioRequest request
        ) {

        return ResponseEntity.ok(
            usuarioService
                .atualizarUsuario(
                    id,
                    request
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse>
        login(
            @Valid
            @RequestBody
            LoginRequest request
        ) {

        return ResponseEntity.ok(
            usuarioService
                .autenticar(request)
        );
    }
}