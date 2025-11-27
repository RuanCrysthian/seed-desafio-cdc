package com.rfdev.desafio_cdc.livro.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteApiSetup;
import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CadastroLivroControllerTest extends TesteApiSetup {

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
    void deveCadastrarLivroComSucesso() throws Exception {
        Categoria categoria = new Categoria("Tecnologia");
        entityManager.persist(categoria);
        entityManager.flush();
        entityManager.clear();
        Autor autor = new Autor("Fulano de Tal", "fulano.tal@email.com", "Autor de livros de tecnologia.");
        entityManager.persist(autor);
        entityManager.flush();
        entityManager.clear();
        CadastroLivroRequest request = new CadastroLivroRequest(
            "Aprendendo Spring Boot",
            "Um guia completo para desenvolver aplicações com Spring Boot.",
            "Sumario do livro de Spring Boot.",
            new BigDecimal("49.90"),
            300,
            "1234567890123",
            LocalDateTime.now().plusDays(10),
            categoria.getId(),
            autor.getId()
        );

        mockMvc.perform(post("/api/livros")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.titulo").value("Aprendendo Spring Boot"));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(l) FROM Livro l WHERE l.titulo = :titulo", Long.class)
            .setParameter("titulo", "Aprendendo Spring Boot")
            .getSingleResult();

        Assertions.assertEquals(1L, count);
    }

    @Test
    void naoDeveCadastrarLivroSemCategoriaExistente() throws Exception {
        Autor autor = new Autor("Ciclano de Tal", "ciclano@email.com", "Autor de livros diversos.");
        entityManager.persist(autor);
        entityManager.flush();
        entityManager.clear();
        CadastroLivroRequest request = new CadastroLivroRequest(
            "Livro Sem Categoria",
            "Descrição do livro sem categoria.",
            "Sumario do livro sem categoria.",
            new BigDecimal("39.90"),
            250,
            "9876543210123",
            LocalDateTime.now().plusDays(5),
            UUID.randomUUID(),
            autor.getId()
        );

        mockMvc.perform(post("/api/livros")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.mensagens[0]").value("Categoria não encontrada"));
    }

    @Test
    void naoDeveCadastrarLivroSemAutorExistente() throws Exception {
        Categoria categoria = new Categoria("Ficção");
        entityManager.persist(categoria);
        entityManager.flush();
        entityManager.clear();
        CadastroLivroRequest request = new CadastroLivroRequest(
            "Livro Sem Autor",
            "Descrição do livro sem autor.",
            "Sumario do livro sem autor.",
            new BigDecimal("29.90"),
            200,
            "1928374650123",
            LocalDateTime.now().plusDays(7),
            categoria.getId(),
            UUID.randomUUID()
        );

        mockMvc.perform(post("/api/livros")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.mensagens[0]").value("Autor não encontrado"));
    }
}