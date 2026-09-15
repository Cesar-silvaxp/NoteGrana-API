package com.notegrana.api.service;

import com.notegrana.api.dto.RecuperacaoSenhaResponse;
import com.notegrana.api.dto.RedefinirSenhaRequest;
import com.notegrana.api.dto.SolicitarRecuperacaoSenhaRequest;
import com.notegrana.api.exception.SenhasNaoConferemException;
import com.notegrana.api.exception.TokenRecuperacaoExpiradoException;
import com.notegrana.api.exception.TokenRecuperacaoInvalidoException;
import com.notegrana.api.model.TokenRecuperacaoSenha;
import com.notegrana.api.model.Usuario;
import com.notegrana.api.repository.TokenRecuperacaoSenhaRepository;
import com.notegrana.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    private static final int MINUTOS_VALIDADE = 15;

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoSenhaRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public RecuperacaoSenhaService(
        UsuarioRepository usuarioRepository,
        TokenRecuperacaoSenhaRepository tokenRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RecuperacaoSenhaResponse solicitarRecuperacao(
        SolicitarRecuperacaoSenhaRequest request
    ) {
        String email = request
            .getEmail()
            .trim()
            .toLowerCase();

        Usuario usuario = usuarioRepository
            .findByEmailIgnoreCase(email)
            .orElse(null);

        String mensagem =
            "Se o e-mail estiver cadastrado, uma solicitação de recuperação foi criada.";

        if (usuario == null) {
            return new RecuperacaoSenhaResponse(
                mensagem,
                null
            );
        }

        tokenRepository.deleteByUsuario(usuario);

        String tokenOriginal =
            UUID.randomUUID().toString();

        String tokenHash =
            gerarHashToken(tokenOriginal);

        TokenRecuperacaoSenha token =
            new TokenRecuperacaoSenha(
                tokenHash,
                usuario,
                LocalDateTime.now()
                    .plusMinutes(MINUTOS_VALIDADE)
            );

        tokenRepository.save(token);

        return new RecuperacaoSenhaResponse(
            mensagem,
            tokenOriginal
        );
    }

    @Transactional
    public void redefinirSenha(
        RedefinirSenhaRequest request
    ) {
        if (
            !request
                .getNovaSenha()
                .equals(
                    request
                        .getConfirmacaoNovaSenha()
                )
        ) {
            throw new SenhasNaoConferemException();
        }

        String tokenHash =
            gerarHashToken(
                request.getToken()
            );

        TokenRecuperacaoSenha token =
            tokenRepository
                .findByToken(tokenHash)
                .orElseThrow(
                    TokenRecuperacaoInvalidoException::new
                );

        if (token.isUsado()) {
            throw new TokenRecuperacaoInvalidoException();
        }

        if (token.estaExpirado()) {
            throw new TokenRecuperacaoExpiradoException();
        }

        Usuario usuario =
            token.getUsuario();

        String novaSenhaHash =
            passwordEncoder.encode(
                request.getNovaSenha()
            );

        usuario.setSenhaHash(
            novaSenhaHash
        );

        usuarioRepository.save(usuario);

        token.setUsado(true);

        tokenRepository.save(token);
    }

    private String gerarHashToken(
        String token
    ) {
        try {
            MessageDigest digest =
                MessageDigest.getInstance(
                    "SHA-256"
                );

            byte[] hash =
                digest.digest(
                    token.getBytes(
                        StandardCharsets.UTF_8
                    )
                );

            return HexFormat
                .of()
                .formatHex(hash);

        } catch (
            NoSuchAlgorithmException exception
        ) {
            throw new IllegalStateException(
                "Não foi possível gerar o hash do token.",
                exception
            );
        }
    }
}