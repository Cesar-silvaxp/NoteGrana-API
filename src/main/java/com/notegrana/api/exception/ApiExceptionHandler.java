package com.notegrana.api.exception;

import com.notegrana.api.dto.ErroResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(
        EmailJaCadastradoException.class
    )
    public ResponseEntity<ErroResponse>
        tratarEmailJaCadastrado(
            EmailJaCadastradoException exception
        ) {

        return criarResposta(
            HttpStatus.CONFLICT,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        UsuarioNaoEncontradoException.class
    )
    public ResponseEntity<ErroResponse>
        tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception
        ) {

        return criarResposta(
            HttpStatus.NOT_FOUND,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        CredenciaisInvalidasException.class
    )
    public ResponseEntity<ErroResponse>
        tratarCredenciaisInvalidas(
            CredenciaisInvalidasException exception
        ) {

        return criarResposta(
            HttpStatus.UNAUTHORIZED,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        SenhaAtualIncorretaException.class
    )
    public ResponseEntity<ErroResponse>
        tratarSenhaAtualIncorreta(
            SenhaAtualIncorretaException exception
        ) {

        return criarResposta(
            HttpStatus.UNAUTHORIZED,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        SenhasNaoConferemException.class
    )
    public ResponseEntity<ErroResponse>
        tratarSenhasNaoConferem(
            SenhasNaoConferemException exception
        ) {

        return criarResposta(
            HttpStatus.BAD_REQUEST,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        NovaSenhaIgualAtualException.class
    )
    public ResponseEntity<ErroResponse>
        tratarNovaSenhaIgualAtual(
            NovaSenhaIgualAtualException exception
        ) {

        return criarResposta(
            HttpStatus.BAD_REQUEST,
            exception.getMessage()
        );
    }

    @ExceptionHandler(
        MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErroResponse>
        tratarValidacao(
            MethodArgumentNotValidException exception
        ) {

        Map<String, String> campos =
            new LinkedHashMap<>();

        exception
            .getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                campos.put(
                    error.getField(),
                    error.getDefaultMessage()
                )
            );

        ErroResponse erro =
            new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Existem dados inválidos na requisição.",
                campos
            );

        return ResponseEntity
            .badRequest()
            .body(erro);
    }

    private ResponseEntity<ErroResponse>
        criarResposta(
            HttpStatus status,
            String mensagem
        ) {

        ErroResponse erro =
            new ErroResponse(
                status.value(),
                mensagem,
                null
            );

        return ResponseEntity
            .status(status)
            .body(erro);
    }
}