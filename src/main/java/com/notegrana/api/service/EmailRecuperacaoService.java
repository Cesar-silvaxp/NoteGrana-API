package com.notegrana.api.service;

import com.notegrana.api.exception.EmailRecuperacaoException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class EmailRecuperacaoService {

    private static final String BREVO_API_URL =
        "https://api.brevo.com/v3";

    private final RestClient restClient;
    private final String apiKey;
    private final String remetenteEmail;
    private final String remetenteNome;

    public EmailRecuperacaoService(
        @Value("${brevo.api.key}")
        String apiKey,

        @Value("${brevo.sender.email}")
        String remetenteEmail,

        @Value("${brevo.sender.name}")
        String remetenteNome
    ) {
        this.apiKey =
            apiKey;

        this.remetenteEmail =
            remetenteEmail;

        this.remetenteNome =
            remetenteNome;

        this.restClient =
            RestClient
                .builder()
                .baseUrl(
                    BREVO_API_URL
                )
                .build();
    }

    public void enviarCodigoRecuperacao(
        String destinatario,
        String nomeUsuario,
        String codigo,
        int minutosValidade
    ) {
        Map<String, Object> remetente =
            Map.of(
                "name",
                remetenteNome,
                "email",
                remetenteEmail
            );

        Map<String, Object> destinatarioEmail =
            Map.of(
                "name",
                nomeUsuario,
                "email",
                destinatario
            );

        Map<String, Object> corpo =
            Map.of(
                "sender",
                remetente,

                "to",
                List.of(
                    destinatarioEmail
                ),

                "subject",
                "Código de recuperação - NoteGrana",

                "textContent",
                montarMensagem(
                    nomeUsuario,
                    codigo,
                    minutosValidade
                )
            );

        try {
            restClient
                .post()
                .uri(
                    "/smtp/email"
                )
                .header(
                    "api-key",
                    apiKey
                )
                .header(
                    HttpHeaders.ACCEPT,
                    MediaType.APPLICATION_JSON_VALUE
                )
                .contentType(
                    MediaType.APPLICATION_JSON
                )
                .body(
                    corpo
                )
                .retrieve()
                .toBodilessEntity();

        } catch (
            RestClientException exception
        ) {
            throw new EmailRecuperacaoException(
                "Não foi possível enviar o e-mail de recuperação.",
                exception
            );
        }
    }

    private String montarMensagem(
        String nomeUsuario,
        String codigo,
        int minutosValidade
    ) {
        return """
            Olá, %s.

            Recebemos uma solicitação para redefinir
            a senha da sua conta no NoteGrana.

            Seu código de recuperação é:

            %s

            Esse código é válido por %d minutos.

            Se você não solicitou a recuperação da
            senha, ignore este e-mail.

            Nunca compartilhe este código com outras
            pessoas.

            Equipe NoteGrana
            """.formatted(
                nomeUsuario,
                codigo,
                minutosValidade
            );
    }
}