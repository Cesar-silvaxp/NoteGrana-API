package com.notegrana.api.repository;

import com.notegrana.api.model.Gasto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GastoRepository
    extends JpaRepository<Gasto, Long> {

    List<Gasto>
        findAllByUsuarioIdOrderByDataHoraDesc(
            Long usuarioId
        );

    Optional<Gasto>
        findByIdAndUsuarioId(
            Long id,
            Long usuarioId
        );
}