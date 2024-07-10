package com.teste.calculadora.service;

import com.teste.calculadora.model.Expressao;
import com.teste.calculadora.repository.ExpressaoRepository;

import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacluladoraService {
    
    @Autowired
    private ExpressaoRepository expressaoRepository;

    public double calcularExpressao(String expressao) {
        Expressao expressaoExistente = expressaoRepository.findByExpressao(expressao);
        if (expressaoExistente != null) {
            return expressaoExistente.getResultado();
        }

        double resultado = calcular(expressao);
        Expressao novaExpressao = new Expressao();
        novaExpressao.setExpressao(expressao);
        novaExpressao.setResultado(resultado);
        expressaoRepository.save(novaExpressao);

        return resultado;
    }

    private double calcular(String expressao) {

        expressao = expressao.replaceAll("\\s+", "").replace(',', '.');

        Stack<Double> numeros = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expressao.length() && (Character.isDigit(expressao.charAt(i)) || expressao.charAt(i) == '.')) {
                    sb.append(expressao.charAt(i++));
                }
                i--;
                numeros.push(Double.parseDouble(sb.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                    numeros.push(aplicarOperador(operadores.pop(), numeros.pop(), numeros.pop()));
                }
                operadores.push(c);
            } else {
                throw new IllegalArgumentException("Caractere inválido na expressão");
            }
        }

        while (!operadores.isEmpty()) {
            numeros.push(aplicarOperador(operadores.pop(), numeros.pop(), numeros.pop()));
        }

        return numeros.pop();
    }

    private int precedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private double aplicarOperador(char operador, double b, double a) {
        switch (operador) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Divisão por zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Operador inválido");
        }
    }
}
