package com.notegrana.api.repository;

import com.notegrana.api.model.TokenRecuperacaoSenha;
import com.notegrana.api.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoSenhaRepository
    extends JpaRepository<
        TokenRecuperacaoSenha,
        Long
    > {

    Optional<TokenRecuperacaoSenha>
        findByToken(String token);

    void deleteByUsuario(Usuario usuario);
}