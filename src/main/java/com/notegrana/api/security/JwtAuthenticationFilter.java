package com.notegrana.api.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.notegrana.api.model.Usuario;
import com.notegrana.api.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter
    extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UsuarioRepository usuarioRepository
    ) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization =
            request.getHeader("Authorization");

        if (
            authorization != null &&
            authorization.startsWith("Bearer ")
        ) {
            String token =
                authorization
                    .substring(7)
                    .trim();

            try {
                DecodedJWT jwt =
                    jwtService.validarToken(token);

                Long usuarioId =
                    jwt
                        .getClaim("usuarioId")
                        .asLong();

                if (
                    usuarioId != null &&
                    SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null
                ) {
                    Usuario usuario =
                        usuarioRepository
                            .findById(usuarioId)
                            .orElse(null);

                    if (usuario != null) {
                        UsernamePasswordAuthenticationToken
                            authentication =
                                new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    Collections.emptyList()
                                );

                        authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                        );

                        SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                authentication
                            );
                    }
                }

            } catch (
                JWTVerificationException |
                IllegalArgumentException exception
            ) {
                SecurityContextHolder
                    .clearContext();
            }
        }

        filterChain.doFilter(
            request,
            response
        );
    }
}