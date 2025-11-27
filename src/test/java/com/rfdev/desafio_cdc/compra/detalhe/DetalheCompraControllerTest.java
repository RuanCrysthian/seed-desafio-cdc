package com.rfdev.desafio_cdc.compra.detalhe;

import com.rfdev.desafio_cdc.TesteApiSetup;
import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.compra.Pedido;
import com.rfdev.desafio_cdc.compra.PedidoItem;
import com.rfdev.desafio_cdc.cupom.Cupom;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.pais.Pais;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DetalheCompraControllerTest extends TesteApiSetup {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    void deveRetornarDetalheDaCompraComSucessoQuandoCompraExisteSemCupomESemEstado() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Argentina");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Tecnologia");
        entityManager.persist(categoria);

        Autor autor = new Autor("João Silva", "joao@email.com", "Autor especialista em tecnologia");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Java Moderno",
            "Aprenda Java com as últimas versões",
            "1. Introdução\n2. Conceitos avançados",
            new BigDecimal("89.90"),
            350,
            "ISBN-1234567890",
            LocalDateTime.now().plusDays(30),
            categoria,
            autor
        );
        entityManager.persist(livro);

        PedidoItem item = new PedidoItem(livro, 2);
        Pedido pedido = new Pedido(new BigDecimal("179.80"), List.of(item));
        entityManager.persist(pedido);

        Compra compra = new Compra(
            "cliente@email.com",
            "Pedro",
            "Santos",
            "24441116055",
            "Rua das Flores, 123",
            "Apto 45",
            "Buenos Aires",
            pais,
            null,
            "11999999999",
            "12345678",
            pedido
        );
        entityManager.persist(compra);

        entityManager.flush();
        entityManager.clear();

        // Executar e verificar
        mockMvc.perform(get("/api/compras/{id}", compra.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.email").value("cliente@email.com"))
            .andExpect(jsonPath("$.nomeCompleto").value("Pedro Santos"))
            .andExpect(jsonPath("$.endereco").value("Rua das Flores, 123"))
            .andExpect(jsonPath("$.complemento").value("Apto 45"))
            .andExpect(jsonPath("$.cidade").value("Buenos Aires"))
            .andExpect(jsonPath("$.pais").value("Argentina"))
            .andExpect(jsonPath("$.estado").value("N/A"))
            .andExpect(jsonPath("$.telefone").value("11999999999"))
            .andExpect(jsonPath("$.cep").value("12345678"))
            .andExpect(jsonPath("$.valorOriginal").value(179.80))
            .andExpect(jsonPath("$.possuiCupomDeDesconto").value(false))
            .andExpect(jsonPath("$.valorTotal").value(179.80))
            .andExpect(jsonPath("$.itensComprados").isArray())
            .andExpect(jsonPath("$.itensComprados[0].tituloLivro").value("Java Moderno"))
            .andExpect(jsonPath("$.itensComprados[0].autor").value("João Silva"))
            .andExpect(jsonPath("$.itensComprados[0].quantidade").value(2))
            .andExpect(jsonPath("$.itensComprados[0].precoUnitario").value(89.90));
    }

    @Test
    void deveRetornarDetalheDaCompraComSucessoQuandoCompraExisteComCupomEComEstado() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Brasil");
        entityManager.persist(pais);

        Estado estado = new Estado("São Paulo", pais);
        entityManager.persist(estado);
        pais.adicionarEstado(estado);

        Categoria categoria = new Categoria("Desenvolvimento Web");
        entityManager.persist(categoria);

        Autor autor = new Autor("Maria Silva", "maria@email.com", "Especialista em desenvolvimento web");
        entityManager.persist(autor);

        Livro livro1 = new Livro(
            "React Avançado",
            "Domine React com hooks e context",
            "1. Hooks\n2. Context API\n3. Performance",
            new BigDecimal("120.00"),
            450,
            "ISBN-9876543210",
            LocalDateTime.now().plusDays(25),
            categoria,
            autor
        );
        entityManager.persist(livro1);

        Livro livro2 = new Livro(
            "Node.js Essencial",
            "Aprenda backend com Node.js",
            "1. Fundamentos\n2. Express\n3. Banco de dados",
            new BigDecimal("95.50"),
            380,
            "ISBN-1111222233",
            LocalDateTime.now().plusDays(40),
            categoria,
            autor
        );
        entityManager.persist(livro2);

        Cupom cupom = new Cupom("DESCONTO15", new BigInteger("15"), LocalDateTime.now().plusDays(10));
        entityManager.persist(cupom);

        PedidoItem item1 = new PedidoItem(livro1, 1);
        PedidoItem item2 = new PedidoItem(livro2, 3);
        Pedido pedido = new Pedido(new BigDecimal("406.50"), List.of(item1, item2));
        entityManager.persist(pedido);

        Compra compra = new Compra(
            "usuario@email.com",
            "Ana",
            "Costa",
            "12345678900",
            "Av. Paulista, 1000",
            "Conjunto 85",
            "São Paulo",
            pais,
            estado,
            "11988887777",
            "01310100",
            pedido
        );
        compra.aplicarCupom(cupom);
        entityManager.persist(compra);

        entityManager.flush();
        entityManager.clear();

        // Executar e verificar
        mockMvc.perform(get("/api/compras/{id}", compra.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.email").value("usuario@email.com"))
            .andExpect(jsonPath("$.nomeCompleto").value("Ana Costa"))
            .andExpect(jsonPath("$.endereco").value("Av. Paulista, 1000"))
            .andExpect(jsonPath("$.complemento").value("Conjunto 85"))
            .andExpect(jsonPath("$.cidade").value("São Paulo"))
            .andExpect(jsonPath("$.pais").value("Brasil"))
            .andExpect(jsonPath("$.estado").value("São Paulo"))
            .andExpect(jsonPath("$.telefone").value("11988887777"))
            .andExpect(jsonPath("$.cep").value("01310100"))
            .andExpect(jsonPath("$.valorOriginal").value(406.50))
            .andExpect(jsonPath("$.possuiCupomDeDesconto").value(true))
            .andExpect(jsonPath("$.valorTotal").value(345.52)) // 406.50 - 15% = 345.525 arredondado para 345.53
            .andExpect(jsonPath("$.itensComprados").isArray())
            .andExpect(jsonPath("$.itensComprados").isNotEmpty())
            .andExpect(jsonPath("$.itensComprados[0].tituloLivro").value("React Avançado"))
            .andExpect(jsonPath("$.itensComprados[0].autor").value("Maria Silva"))
            .andExpect(jsonPath("$.itensComprados[0].quantidade").value(1))
            .andExpect(jsonPath("$.itensComprados[0].precoUnitario").value(120.00))
            .andExpect(jsonPath("$.itensComprados[1].tituloLivro").value("Node.js Essencial"))
            .andExpect(jsonPath("$.itensComprados[1].autor").value("Maria Silva"))
            .andExpect(jsonPath("$.itensComprados[1].quantidade").value(3))
            .andExpect(jsonPath("$.itensComprados[1].precoUnitario").value(95.50));
    }

    @Test
    void deveRetornarNotFoundQuandoCompraNaoExiste() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        // Executar e verificar
        mockMvc.perform(get("/api/compras/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarDetalheDaCompraComEstadoNAQuandoEstadoEhNulo() throws Exception {
        // Preparar dados - país sem estados
        Pais pais = new Pais("Uruguai");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Ficção");
        entityManager.persist(categoria);

        Autor autor = new Autor("Carlos Santos", "carlos@email.com", "Escritor de ficção");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Aventuras no Código",
            "Uma história de programação",
            "1. O início\n2. O meio\n3. O fim",
            new BigDecimal("45.00"),
            200,
            "ISBN-5555666677",
            LocalDateTime.now().plusDays(15),
            categoria,
            autor
        );
        entityManager.persist(livro);

        PedidoItem item = new PedidoItem(livro, 1);
        Pedido pedido = new Pedido(new BigDecimal("45.00"), List.of(item));
        entityManager.persist(pedido);

        Compra compra = new Compra(
            "leitor@email.com",
            "José",
            "Silva",
            "98765432100",
            "Rua Principal, 456",
            "Casa",
            "Montevideo",
            pais,
            null, // Estado é null
            "59988776655",
            "87654321",
            pedido
        );
        entityManager.persist(compra);

        entityManager.flush();
        entityManager.clear();

        // Executar e verificar
        mockMvc.perform(get("/api/compras/{id}", compra.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.estado").value("N/A"))
            .andExpect(jsonPath("$.email").value("leitor@email.com"))
            .andExpect(jsonPath("$.nomeCompleto").value("José Silva"))
            .andExpect(jsonPath("$.pais").value("Uruguai"));
    }

    @Test
    void deveRetornarBadRequestQuandoIdEhInvalido() throws Exception {
        String idInvalido = "id-invalido";

        // Executar e verificar
        mockMvc.perform(get("/api/compras/{id}", idInvalido)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
