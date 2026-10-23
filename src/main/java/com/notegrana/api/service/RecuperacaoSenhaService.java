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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class RecuperacaoSenhaService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(
            RecuperacaoSenhaService.class
        );

    private static final int MINUTOS_VALIDADE =
        15;

    private static final int TAMANHO_CODIGO =
        10;

    private static final char[] CARACTERES_CODIGO =
        "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
            .toCharArray();

    private static final SecureRandom SECURE_RANDOM =
        new SecureRandom();

    private static final String MENSAGEM_GENERICA =
        "Se o e-mail estiver cadastrado, "
            + "enviaremos as instruções de recuperação.";

    private final UsuarioRepository usuarioRepository;

    private final TokenRecuperacaoSenhaRepository
        tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailRecuperacaoService
        emailRecuperacaoService;

    public RecuperacaoSenhaService(
        UsuarioRepository usuarioRepository,
        TokenRecuperacaoSenhaRepository tokenRepository,
        PasswordEncoder passwordEncoder,
        EmailRecuperacaoService emailRecuperacaoService
    ) {
        this.usuarioRepository =
            usuarioRepository;

        this.tokenRepository =
            tokenRepository;

        this.passwordEncoder =
            passwordEncoder;

        this.emailRecuperacaoService =
            emailRecuperacaoService;
    }

    @Transactional
    public RecuperacaoSenhaResponse solicitarRecuperacao(
        SolicitarRecuperacaoSenhaRequest request
    ) {
        String email =
            request
                .getEmail()
                .trim()
                .toLowerCase();

        Usuario usuario =
            usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElse(null);

        if (usuario == null) {
            LOGGER.info(
                "Solicitação de recuperação recebida para e-mail não cadastrado."
            );

            return criarRespostaGenerica();
        }

        tokenRepository.deleteByUsuario(
            usuario
        );

        String codigoOriginal =
            gerarCodigoRecuperacao();

        String codigoHash =
            gerarHashToken(
                codigoOriginal
            );

        TokenRecuperacaoSenha token =
            new TokenRecuperacaoSenha(
                codigoHash,
                usuario,
                LocalDateTime
                    .now()
                    .plusMinutes(
                        MINUTOS_VALIDADE
                    )
            );

        tokenRepository.save(token);

        try {
            emailRecuperacaoService
                .enviarCodigoRecuperacao(
                    usuario.getEmail(),
                    usuario.getNome(),
                    codigoOriginal,
                    MINUTOS_VALIDADE
                );

            LOGGER.info(
                "E-mail de recuperação enviado com sucesso."
            );

        } catch (MailException exception) {

            tokenRepository.delete(
                token
            );

            LOGGER.error(
                "Falha ao enviar e-mail de recuperação: {}",
                exception.getMessage()
            );
        }

        return criarRespostaGenerica();
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

        String codigoNormalizado =
            normalizarCodigo(
                request.getToken()
            );

        String codigoHash =
            gerarHashToken(
                codigoNormalizado
            );

        TokenRecuperacaoSenha token =
            tokenRepository
                .findByToken(
                    codigoHash
                )
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

        usuarioRepository.save(
            usuario
        );

        token.setUsado(true);

        tokenRepository.save(
            token
        );
    }

    private RecuperacaoSenhaResponse
        criarRespostaGenerica() {

        return new RecuperacaoSenhaResponse(
            MENSAGEM_GENERICA
        );
    }

    private String gerarCodigoRecuperacao() {
        StringBuilder codigo =
            new StringBuilder(
                TAMANHO_CODIGO
            );

        for (
            int indice = 0;
            indice < TAMANHO_CODIGO;
            indice++
        ) {
            int posicao =
                SECURE_RANDOM.nextInt(
                    CARACTERES_CODIGO.length
                );

            codigo.append(
                CARACTERES_CODIGO[
                    posicao
                ]
            );
        }

        return codigo.toString();
    }

    private String normalizarCodigo(
        String codigo
    ) {
        return codigo
            .trim()
            .toUpperCase();
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