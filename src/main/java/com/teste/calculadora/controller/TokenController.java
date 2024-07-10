package com.teste.calculadora.controller;

import com.teste.calculadora.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/gerarToken")
    public String gerarToken() {
        return tokenService.gerarToken();
    }
}