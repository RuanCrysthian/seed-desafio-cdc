package com.rfdev.desafio_cdc.categoria.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteSistemaSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CadastroCategoriaControllerTest extends TesteSistemaSetup {

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
    void deveCadastrarCategoriaComSucesso() throws Exception {
        CadastroCategoriaRequest request = new CadastroCategoriaRequest("Ficção Científica");
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(result -> {
                System.out.println("Response Status: " + result.getResponse().getStatus());
                System.out.println("Response Body: " + result.getResponse().getContentAsString());
            })
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nome").value("Ficção Científica"));
    }

    @Test
    void naoDeveCadastrarCategoriaComNomeDuplicado() throws Exception {
        // Primeiro cadastro
        CadastroCategoriaRequest request1 = new CadastroCategoriaRequest("Romance");
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isOk());

        // Segundo cadastro com nome duplicado
        CadastroCategoriaRequest request2 = new CadastroCategoriaRequest("Romance");
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andDo(result -> {
                System.out.println("Response Status: " + result.getResponse().getStatus());
                System.out.println("Response Body: " + result.getResponse().getContentAsString());
            })
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens[0]").value("Nome já cadastrado"));
    }
}