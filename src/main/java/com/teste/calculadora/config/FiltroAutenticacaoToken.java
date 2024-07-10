package com.teste.calculadora.config;

import com.teste.calculadora.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FiltroAutenticacaoToken extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (tokenService.validarToken(token)) {
                String username = tokenService.obterUsuarioDoToken(token);
                AutenticacaoToken autenticacaoToken = new AutenticacaoToken(username);
                SecurityContextHolder.getContext().setAuthentication(autenticacaoToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}