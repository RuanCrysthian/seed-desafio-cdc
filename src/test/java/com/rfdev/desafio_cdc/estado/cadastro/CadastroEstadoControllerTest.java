package com.rfdev.desafio_cdc.estado.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteApiSetup;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CadastroEstadoControllerTest extends TesteApiSetup {

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
    void deveCadastrarEstadoComSucesso() throws Exception {
        Pais pais = new Pais("Brasil");
        entityManager.persist(pais);
        entityManager.flush();
        entityManager.clear();
        CadastroEstadoRequest request = new CadastroEstadoRequest("São Paulo", pais.getId());

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nome").value("São Paulo"));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(e) FROM Estado e WHERE e.nome = :nome", Long.class)
            .setParameter("nome", "São Paulo")
            .getSingleResult();

        Assertions.assertEquals(1L, count);
    }

    @Test
    void naoDeveCadastrarEstadoComNomeDuplicadoNoMesmoPais() throws Exception {
        Pais pais = new Pais("Argentina");
        entityManager.persist(pais);
        Estado estadoExistente = new Estado("Buenos Aires", pais);
        entityManager.persist(estadoExistente);
        entityManager.flush();
        entityManager.clear();

        CadastroEstadoRequest request = new CadastroEstadoRequest("Buenos Aires", pais.getId());

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens[0]").value("Já existe um estado com esse nome"));
    }

    @Test
    void naoDeveCadastrarEstadoComPaisInexistente() throws Exception {
        CadastroEstadoRequest request = new CadastroEstadoRequest("Córdoba", java.util.UUID.randomUUID());

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens[0]").value("Pais não encontrado"));
    }
}