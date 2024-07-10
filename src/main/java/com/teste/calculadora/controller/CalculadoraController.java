package com.teste.calculadora.controller;

import com.teste.calculadora.service.CacluladoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/calculadora")
public class CalculadoraController {

    @Autowired
    private CacluladoraService calculadoraService;

    @PostMapping("/calcular")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> calcularExpressao(@RequestBody String expressao) {
        try {
            double resultado = calculadoraService.calcularExpressao(expressao);
            return ResponseEntity.ok(Collections.singletonMap("resultado", String.format("%.2f", resultado)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}