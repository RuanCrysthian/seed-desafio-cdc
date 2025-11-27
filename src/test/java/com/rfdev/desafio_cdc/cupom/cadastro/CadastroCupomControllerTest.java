package com.rfdev.desafio_cdc.cupom.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteApiSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CadastroCupomControllerTest extends TesteApiSetup {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    void deveCadastrarCupomComSucesso() throws Exception {
        CadastroCupomRequest request = new CadastroCupomRequest("CUPOM10", new BigInteger("10"), LocalDateTime.now().plusDays(10));

        mockMvc.perform(post("/api/cupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.codigo").value("CUPOM10"));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Cupom c WHERE c.codigo = :codigo", Long.class)
            .setParameter("codigo", "CUPOM10")
            .getSingleResult();
        Assertions.assertEquals(1L, count);
    }

    @Test
    void naoDeveCadastrarCupomComCodigoDuplicado() throws Exception {
        CadastroCupomRequest request = new CadastroCupomRequest("CUPOM20", new BigInteger("20"), LocalDateTime.now().plusDays(10));

        mockMvc.perform(post("/api/cupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/cupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarCupomComDataExpiracaoNoPassado() throws Exception {
        CadastroCupomRequest request = new CadastroCupomRequest("CUPOM30", new BigInteger("30"), LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/api/cupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}