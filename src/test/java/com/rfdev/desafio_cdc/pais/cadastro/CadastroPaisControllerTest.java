package com.rfdev.desafio_cdc.pais.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteSistemaSetup;
import com.rfdev.desafio_cdc.pais.Pais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CadastroPaisControllerTest extends TesteSistemaSetup {

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
    void deveCadastrarPaisComSucesso() throws Exception {
        CadastroPaisRequest request = new CadastroPaisRequest("Brasil");

        mockMvc.perform(post("/api/paises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nome").value("Brasil"));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(p) FROM Pais p WHERE p.nome = :nome", Long.class)
            .setParameter("nome", "Brasil")
            .getSingleResult();
        assertEquals(1L, count);
    }

    @Test
    void naoDeveCadastrarPaisComNomeDuplicado() throws Exception {
        entityManager.persist(new Pais("Argentina"));
        entityManager.flush();
        entityManager.clear();

        CadastroPaisRequest request = new CadastroPaisRequest("Argentina");

        mockMvc.perform(post("/api/paises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens[0]").value("Já existe um pais cadastrado com esse nome"));
    }


}