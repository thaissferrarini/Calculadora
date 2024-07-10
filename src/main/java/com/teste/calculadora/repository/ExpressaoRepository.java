package com.teste.calculadora.repository;

import com.teste.calculadora.model.Expressao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpressaoRepository extends JpaRepository<Expressao, Long> {
    Expressao findByExpressao(String expressao);
}
