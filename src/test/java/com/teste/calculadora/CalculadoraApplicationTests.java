package com.teste.calculadora;
import com.teste.calculadora.service.CacluladoraService;
import com.teste.calculadora.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculadoraApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    public void setup() {
        token = tokenService.gerarToken();
    }

    @Test
    public void deveRetornarValorParaExpressaoSomaSimples() throws Exception {
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("2+2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultado\":\"4,00\"}"));
    }

    @Test
    public void deveRetornarValorParaExpressaoSomaComDecimais() throws Exception {
        // Primeira chamada
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("2.2+2.2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultado\":\"4,40\"}"));

        // Segunda chamada, deve usar o resultado previamente gravado no banco
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("2.2+2.2"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultado\":\"4,40\"}"));
    }

    @Test
    public void deveRetornarValorParaExpressaoComMultiplicacaoESoma() throws Exception {
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("2.3*2.3+5"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultado\":\"10,29\"}"));
    }

    @Test
    public void deveRetornarValorParaExpressaoComDivisao() throws Exception {
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("2.33/3"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"resultado\":\"0,78\"}"));
    }

    @Test
    public void deveRetornarErroParaDivisaoPorZero() throws Exception {
        mockMvc.perform(post("/calculadora/calcular")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.TEXT_PLAIN)
                .content("1/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Divisão por zero\"}"));
    }
}