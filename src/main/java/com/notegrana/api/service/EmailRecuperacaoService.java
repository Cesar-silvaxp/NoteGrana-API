package com.notegrana.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailRecuperacaoService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String remetente;

    public EmailRecuperacaoService(
        JavaMailSender mailSender
    ) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoRecuperacao(
        String destinatario,
        String nomeUsuario,
        String codigo,
        int minutosValidade
    ) {
        SimpleMailMessage mensagem =
            new SimpleMailMessage();

        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);

        mensagem.setSubject(
            "Código de recuperação - NoteGrana"
        );

        mensagem.setText(
            montarMensagem(
                nomeUsuario,
                codigo,
                minutosValidade
            )
        );

        mailSender.send(mensagem);
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