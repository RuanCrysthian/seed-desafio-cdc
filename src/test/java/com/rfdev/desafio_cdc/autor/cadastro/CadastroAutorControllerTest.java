package com.rfdev.desafio_cdc.autor.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteApiSetup;
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


class CadastroAutorControllerTest extends TesteApiSetup {

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
    void deveCadastrarAutorComSucesso() throws Exception {
        // Given
        CadastroAutorRequest request = new CadastroAutorRequest(
            "João Silva",
            "joao.silva@email.com",
            "Descrição do autor João Silva"
        );

        // When & Then
        mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
            .andExpect(jsonPath("$.descricao").value("Descrição do autor João Silva"));

        // Verify database persistence
        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(a) FROM Autor a WHERE a.email = :email", Long.class)
            .setParameter("email", "joao.silva@email.com")
            .getSingleResult();

        assertEquals(1L, count);
    }

    @Test
    void naoDeveCadastrarAutorComDadosInvalidos() throws Exception {
        // Given
        CadastroAutorRequest request = new CadastroAutorRequest(
            "",
            "email-invalido",
            ""
        );

        // When & Then
        mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarAutorComEmailDuplicado() throws Exception {
        // Given
        CadastroAutorRequest firstRequest = new CadastroAutorRequest(
            "Primeiro Autor",
            "autor@email.com",
            "Primeira descrição"
        );

        CadastroAutorRequest secondRequest = new CadastroAutorRequest(
            "Segundo Autor",
            "autor@email.com",
            "Segunda descrição"
        );

        // When
        mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
            .andExpect(status().isOk());

        // Then
        mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRequest)))
            .andExpect(status().isBadRequest());
    }


}