package com.notegrana.api.exception;

public class EmailJaCadastradoException
    extends RuntimeException {

    public EmailJaCadastradoException() {
        super("Já existe um usuário cadastrado com este e-mail.");
    }
}