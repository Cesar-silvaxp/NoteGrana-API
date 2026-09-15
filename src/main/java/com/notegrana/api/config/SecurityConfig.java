package com.notegrana.api.config;

import com.notegrana.api.security.JwtAuthenticationEntryPoint;
import com.notegrana.api.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter
        jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint
        jwtAuthenticationEntryPoint;

    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.jwtAuthenticationFilter =
            jwtAuthenticationFilter;

        this.jwtAuthenticationEntryPoint =
            jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http
    ) throws Exception {

        http
            .csrf(csrf ->
                csrf.disable()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .formLogin(form ->
                form.disable()
            )
            .httpBasic(basic ->
                basic.disable()
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                    jwtAuthenticationEntryPoint
                )
            )
            .authorizeHttpRequests(authorize ->
                authorize

                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/usuarios",
                        "/api/usuarios/login",
                        "/api/recuperacao-senha/solicitar",
                        "/api/recuperacao-senha/redefinir"
                    )
                    .permitAll()

                    .requestMatchers(
                        "/error"
                    )
                    .permitAll()

                    .anyRequest()
                    .authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}