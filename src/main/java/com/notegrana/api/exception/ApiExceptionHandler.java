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

        ErroResponse erro =
            new ErroResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                null
            );

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(erro);
    }

    @ExceptionHandler(
        UsuarioNaoEncontradoException.class
    )
    public ResponseEntity<ErroResponse>
        tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception
        ) {

        ErroResponse erro =
            new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                null
            );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(erro);
    }

    @ExceptionHandler(
        CredenciaisInvalidasException.class
    )
    public ResponseEntity<ErroResponse>
        tratarCredenciaisInvalidas(
            CredenciaisInvalidasException exception
        ) {

        ErroResponse erro =
            new ErroResponse(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                null
            );

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(erro);
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
}