package com.notegrana.api.repository;

import com.notegrana.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
    extends JpaRepository<Usuario, Long> {

    boolean existsByEmailIgnoreCase(
        String email
    );

    boolean existsByEmailIgnoreCaseAndIdNot(
        String email,
        Long id
    );

    Optional<Usuario> findByEmailIgnoreCase(
        String email
    );
}