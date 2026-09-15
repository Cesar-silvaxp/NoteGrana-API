package com.notegrana.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.notegrana.api.model.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private static final String EMISSOR =
        "notegrana-api";

    private static final Duration DURACAO_TOKEN =
        Duration.ofHours(8);

    private final Algorithm algorithm;

    public JwtService(
        @Value("${security.jwt.secret}")
        String secret
    ) {
        if (
            secret == null ||
            secret.length() < 32
        ) {
            throw new IllegalStateException(
                "JWT_SECRET deve possuir pelo menos 32 caracteres."
            );
        }

        this.algorithm =
            Algorithm.HMAC256(secret);
    }

    public TokenGerado gerarToken(
        Usuario usuario
    ) {
        Instant agora =
            Instant.now();

        Instant expiraEm =
            agora.plus(DURACAO_TOKEN);

        String token =
            JWT.create()
                .withIssuer(EMISSOR)
                .withSubject(
                    usuario.getEmail()
                )
                .withClaim(
                    "usuarioId",
                    usuario.getId()
                )
                .withIssuedAt(
                    Date.from(agora)
                )
                .withExpiresAt(
                    Date.from(expiraEm)
                )
                .sign(algorithm);

        return new TokenGerado(
            token,
            expiraEm
        );
    }

    public DecodedJWT validarToken(
        String token
    ) {
        return JWT
            .require(algorithm)
            .withIssuer(EMISSOR)
            .build()
            .verify(token);
    }

    public record TokenGerado(
        String token,
        Instant expiraEm
    ) {
    }
}