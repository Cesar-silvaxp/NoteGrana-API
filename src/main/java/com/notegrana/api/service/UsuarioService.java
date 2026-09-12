package com.notegrana.api.service;

import com.notegrana.api.dto.AtualizarUsuarioRequest;
import com.notegrana.api.dto.CriarUsuarioRequest;
import com.notegrana.api.dto.LoginRequest;
import com.notegrana.api.dto.UsuarioResponse;
import com.notegrana.api.exception.CredenciaisInvalidasException;
import com.notegrana.api.exception.EmailJaCadastradoException;
import com.notegrana.api.exception.UsuarioNaoEncontradoException;
import com.notegrana.api.model.Usuario;
import com.notegrana.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository =
            usuarioRepository;

        this.passwordEncoder =
            passwordEncoder;
    }

    public UsuarioResponse criarUsuario(
        CriarUsuarioRequest request
    ) {
        String nome =
            request.getNome().trim();

        String email =
            normalizarEmail(
                request.getEmail()
            );

        if (
            usuarioRepository
                .existsByEmailIgnoreCase(email)
        ) {
            throw new EmailJaCadastradoException();
        }

        String senhaHash =
            passwordEncoder.encode(
                request.getSenha()
            );

        Usuario usuario =
            new Usuario(
                nome,
                email,
                senhaHash
            );

        Usuario usuarioSalvo =
            usuarioRepository.save(usuario);

        return converterParaResponse(
            usuarioSalvo
        );
    }

    public List<UsuarioResponse>
        listarUsuarios() {

        return usuarioRepository
            .findAll()
            .stream()
            .map(this::converterParaResponse)
            .toList();
    }

    public UsuarioResponse buscarPorId(
        Long id
    ) {
        Usuario usuario =
            buscarEntidadePorId(id);

        return converterParaResponse(
            usuario
        );
    }

    public UsuarioResponse atualizarUsuario(
        Long id,
        AtualizarUsuarioRequest request
    ) {
        Usuario usuario =
            buscarEntidadePorId(id);

        String nome =
            request.getNome().trim();

        String email =
            normalizarEmail(
                request.getEmail()
            );

        boolean emailEmUso =
            usuarioRepository
                .existsByEmailIgnoreCaseAndIdNot(
                    email,
                    id
                );

        if (emailEmUso) {
            throw new EmailJaCadastradoException();
        }

        usuario.setNome(nome);
        usuario.setEmail(email);

        Usuario atualizado =
            usuarioRepository.save(usuario);

        return converterParaResponse(
            atualizado
        );
    }

    public UsuarioResponse autenticar(
        LoginRequest request
    ) {
        String email =
            normalizarEmail(
                request.getEmail()
            );

        Usuario usuario =
            usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(
                    CredenciaisInvalidasException::new
                );

        boolean senhaCorreta =
            passwordEncoder.matches(
                request.getSenha(),
                usuario.getSenhaHash()
            );

        if (!senhaCorreta) {
            throw new CredenciaisInvalidasException();
        }

        return converterParaResponse(
            usuario
        );
    }

    private Usuario buscarEntidadePorId(
        Long id
    ) {
        return usuarioRepository
            .findById(id)
            .orElseThrow(
                UsuarioNaoEncontradoException::new
            );
    }

    private String normalizarEmail(
        String email
    ) {
        return email
            .trim()
            .toLowerCase();
    }

    private UsuarioResponse converterParaResponse(
        Usuario usuario
    ) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getCriadoEm()
        );
    }
}