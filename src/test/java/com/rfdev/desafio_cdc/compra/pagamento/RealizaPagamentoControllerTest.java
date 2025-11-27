package com.rfdev.desafio_cdc.compra.pagamento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfdev.desafio_cdc.TesteApiSetup;
import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.cupom.Cupom;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.pais.Pais;
import org.junit.jupiter.api.Assertions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RealizaPagamentoControllerTest extends TesteApiSetup {

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
    void deveRealizarPagamentoComSucessoSemCupomEPaisSemEstados() throws Exception {
        Pais pais = new Pais("Argentina");
        entityManager.persist(pais);
        entityManager.flush();
        entityManager.clear();

        Categoria categoria = new Categoria("Tecnologia");
        entityManager.persist(categoria);
        entityManager.flush();
        entityManager.clear();

        Autor autor = new Autor("Autor Teste", "autor@email.com", "Descrição do autor");
        entityManager.persist(autor);
        entityManager.flush();
        entityManager.clear();

        Livro livro = new Livro(
            "Java 21",
            "Resumo do livro de Java",
            "Sumário do livro",
            new BigDecimal("100.00"),
            300,
            "ISBN-1234567890",
            LocalDateTime.now().plusDays(30),
            categoria,
            autor
        );
        entityManager.persist(livro);
        entityManager.flush();
        entityManager.clear();

        // Criar request
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador@email.com",
            "João",
            "Silva",
            "24441116055",
            "Rua Teste, 123",
            "Apto 45",
            "São Paulo",
            pais.getId(),
            null,
            "11999999999",
            "01234567",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("100.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(result -> {
                System.out.println("Response Status: " + result.getResponse().getStatus());
                System.out.println("Response Body: " + result.getResponse().getContentAsString());
            })
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.emailComprador").value("comprador@email.com"))
            .andExpect(jsonPath("$.valorPedido").value(100.00))
            .andExpect(jsonPath("$.codigoCupom").value("N/A"))
            .andExpect(jsonPath("$.valorFinal").value(100.00))
            .andExpect(jsonPath("$.itensComprados").isArray())
            .andExpect(jsonPath("$.itensComprados[0].quantidade").value(1));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Compra c WHERE c.email = :email", Long.class)
            .setParameter("email", "comprador@email.com")
            .getSingleResult();

        Assertions.assertEquals(1L, count);
    }

    @Test
    void deveRealizarPagamentoComSucessoComCupomValidoEPaisComEstado() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Brasil");
        entityManager.persist(pais);

        Estado estado = new Estado("São Paulo", pais);
        entityManager.persist(estado);
        pais.adicionarEstado(estado);

        Categoria categoria = new Categoria("Programação");
        entityManager.persist(categoria);

        Autor autor = new Autor("Maria Silva", "maria@email.com", "Autora de livros técnicos");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Spring Boot Avançado",
            "Resumo do livro de Spring Boot",
            "Sumário completo",
            new BigDecimal("150.00"),
            400,
            "ISBN-9876543210",
            LocalDateTime.now().plusDays(20),
            categoria,
            autor
        );
        entityManager.persist(livro);

        Cupom cupom = new Cupom("DESCONTO20", new BigInteger("20"), LocalDateTime.now().plusDays(10));
        entityManager.persist(cupom);

        entityManager.flush();
        entityManager.clear();

        // Criar request
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "cliente@email.com",
            "Pedro",
            "Santos",
            "24441116055",
            "Av Principal, 456",
            "Casa",
            "São Paulo",
            pais.getId(),
            estado.getId(),
            "11988888888",
            "12345678",
            "DESCONTO20",
            new CarrinhoDeCompraRequest(
                new BigDecimal("150.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.emailComprador").value("cliente@email.com"))
            .andExpect(jsonPath("$.valorPedido").value(150.00))
            .andExpect(jsonPath("$.codigoCupom").value("DESCONTO20"))
            .andExpect(jsonPath("$.valorFinal").value(120.00));

        entityManager.flush();
        entityManager.clear();

        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Compra c WHERE c.email = :email", Long.class)
            .setParameter("email", "cliente@email.com")
            .getSingleResult();

        Assertions.assertEquals(1L, count);
    }

    @Test
    void deveRealizarPagamentoSemCupom() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Chile");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Ficção");
        entityManager.persist(categoria);

        Autor autor = new Autor("Carlos Souza", "carlos@email.com", "Escritor de ficção");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "O Mundo Digital",
            "Resumo de ficção científica",
            "Sumário interessante",
            new BigDecimal("80.00"),
            250,
            "ISBN-5555555555",
            LocalDateTime.now().plusDays(15),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request sem cupom
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador2@email.com",
            "Ana",
            "Costa",
            "24441116055",
            "Rua Secundária, 789",
            "Bloco B",
            "Santiago",
            pais.getId(),
            null,
            "11977777777",
            "87654321",
            null,  // Sem cupom
            new CarrinhoDeCompraRequest(
                new BigDecimal("80.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar - sem cupom
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.emailComprador").value("comprador2@email.com"))
            .andExpect(jsonPath("$.valorPedido").value(80.00))
            .andExpect(jsonPath("$.codigoCupom").value("N/A"))
            .andExpect(jsonPath("$.valorFinal").value(80.00));
    }

    @Test
    void deveRealizarPagamentoComMultiplosLivros() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Portugal");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Educação");
        entityManager.persist(categoria);

        Autor autor = new Autor("José Santos", "jose@email.com", "Professor e escritor");
        entityManager.persist(autor);

        Livro livro1 = new Livro(
            "Matemática Básica",
            "Resumo de matemática",
            "Sumário de matemática",
            new BigDecimal("50.00"),
            200,
            "ISBN-1111111111",
            LocalDateTime.now().plusDays(10),
            categoria,
            autor
        );
        entityManager.persist(livro1);

        Livro livro2 = new Livro(
            "Física Avançada",
            "Resumo de física",
            "Sumário de física",
            new BigDecimal("75.00"),
            350,
            "ISBN-2222222222",
            LocalDateTime.now().plusDays(10),
            categoria,
            autor
        );
        entityManager.persist(livro2);

        entityManager.flush();
        entityManager.clear();

        // Criar request - Total: 50 + (75 * 2) = 200
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "estudante@email.com",
            "Lucas",
            "Oliveira",
            "24441116055",
            "Rua da Escola, 100",
            "Sala 5",
            "Lisboa",
            pais.getId(),
            null,
            "11966666666",
            "11223344",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("200.00"),
                List.of(
                    new ItemCarrinhoRequest(livro1.getId(), 1),
                    new ItemCarrinhoRequest(livro2.getId(), 2)
                )
            )
        );

        // Executar e verificar
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.emailComprador").value("estudante@email.com"))
            .andExpect(jsonPath("$.valorPedido").value(200.00))
            .andExpect(jsonPath("$.itensComprados").isArray())
            .andExpect(jsonPath("$.itensComprados.length()").value(2));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoPaisTemEstadosEEstadoNaoInformado() throws Exception {
        Pais pais = new Pais("México");
        entityManager.persist(pais);

        Estado estado = new Estado("Cidade do México", pais);
        entityManager.persist(estado);
        pais.adicionarEstado(estado);

        Categoria categoria = new Categoria("História");
        entityManager.persist(categoria);

        Autor autor = new Autor("Fernando Lima", "fernando@email.com", "Historiador");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "História Mundial",
            "Resumo de história",
            "Sumário de história",
            new BigDecimal("90.00"),
            300,
            "ISBN-3333333333",
            LocalDateTime.now().plusDays(25),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador3@email.com",
            "Roberto",
            "Alves",
            "24441116055",
            "Calle Principal, 200",
            "Piso 3",
            "CDMX",
            pais.getId(),
            null,  // Estado não informado, mas país tem estados
            "11955555555",
            "55555555",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("90.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("Estado é obrigatório para o país selecionado."));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoEstadoNaoPertenceAoPais() throws Exception {
        // Preparar dados
        Pais paisBrasil = new Pais("Brasil");
        entityManager.persist(paisBrasil);

        Estado estadoBrasil = new Estado("Rio de Janeiro", paisBrasil);
        entityManager.persist(estadoBrasil);
        paisBrasil.adicionarEstado(estadoBrasil);

        Pais paisArgentina = new Pais("Argentina");
        entityManager.persist(paisArgentina);

        Estado estadoArgentina = new Estado("Buenos Aires", paisArgentina);
        entityManager.persist(estadoArgentina);
        paisArgentina.adicionarEstado(estadoArgentina);

        Categoria categoria = new Categoria("Geografia");
        entityManager.persist(categoria);

        Autor autor = new Autor("Paula Fernandes", "paula@email.com", "Geógrafa");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Geografia da América do Sul",
            "Resumo de geografia",
            "Sumário de geografia",
            new BigDecimal("120.00"),
            280,
            "ISBN-4444444444",
            LocalDateTime.now().plusDays(18),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com estado de um país diferente
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador4@email.com",
            "Carla",
            "Ribeiro",
            "24441116055",
            "Rua Internacional, 300",
            "Apt 10",
            "Rio de Janeiro",
            paisBrasil.getId(),
            estadoArgentina.getId(),  // Estado da Argentina no país Brasil
            "11944444444",
            "22222222",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("120.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("O estado selecionado não pertence ao país informado."));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoTotalInformadoNaoCorrespondeAosSomaDosItens() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Uruguai");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Ciência");
        entityManager.persist(categoria);

        Autor autor = new Autor("Ricardo Gomes", "ricardo@email.com", "Cientista");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Física Quântica",
            "Resumo de física quântica",
            "Sumário detalhado",
            new BigDecimal("100.00"),
            400,
            "ISBN-6666666666",
            LocalDateTime.now().plusDays(22),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com total incorreto
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador5@email.com",
            "Marcos",
            "Pereira",
            "24441116055",
            "Av Central, 400",
            "Casa 1",
            "Montevidéu",
            pais.getId(),
            null,
            "11933333333",
            "33333333",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("50.00"),  // Total incorreto (deveria ser 100.00)
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("O total informado não corresponde à soma dos itens no carrinho."));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoLivroNaoExiste() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Colômbia");
        entityManager.persist(pais);

        entityManager.flush();
        entityManager.clear();

        // Criar request com livro inexistente
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador6@email.com",
            "Juliana",
            "Martins",
            "24441116055",
            "Calle 50, 500",
            "Oficina 12",
            "Bogotá",
            pais.getId(),
            null,
            "11922222222",
            "44444444",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("100.00"),
                List.of(new ItemCarrinhoRequest(UUID.randomUUID(), 1))  // UUID aleatório
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("Livro não encontrado"));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoPaisNaoExiste() throws Exception {
        // Preparar dados
        Categoria categoria = new Categoria("Arte");
        entityManager.persist(categoria);

        Autor autor = new Autor("Beatriz Costa", "beatriz@email.com", "Artista");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "História da Arte",
            "Resumo de arte",
            "Sumário de arte",
            new BigDecimal("85.00"),
            320,
            "ISBN-7777777777",
            LocalDateTime.now().plusDays(12),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com país inexistente
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador7@email.com",
            "Rafael",
            "Souza",
            "24441116055",
            "Rua das Flores, 600",
            "Lote 8",
            "Cidade Qualquer",
            UUID.randomUUID(),  // UUID aleatório
            null,
            "11911111111",
            "66666666",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("85.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("Pais não encontrado"));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoEmailInvalido() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Peru");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Culinária");
        entityManager.persist(categoria);

        Autor autor = new Autor("Renata Alves", "renata@email.com", "Chef");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Receitas do Mundo",
            "Resumo de culinária",
            "Sumário de receitas",
            new BigDecimal("60.00"),
            180,
            "ISBN-8888888888",
            LocalDateTime.now().plusDays(8),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com email inválido
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "email-invalido",  // Email sem @
            "Fernanda",
            "Lima",
            "24441116055",
            "Av das Américas, 700",
            "Loja 5",
            "Lima",
            pais.getId(),
            null,
            "11900000000",
            "77777777",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("60.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("O campo Email possui um formato invalido"));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoCamposObrigatoriosEstaoVazios() throws Exception {
        // Criar request com campos vazios
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "",  // email vazio
            "",  // nome vazio
            "",  // sobrenome vazio
            "",  // documento vazio
            "",  // endereco vazio
            "",  // complemento vazio
            "",  // cidade vazio
            null,  // paisId null
            null,
            "",  // telefone vazio
            "",  // cep vazio
            null,
            null  // carrinho null
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens").isArray());
    }

    @Test
    void naoDeveRealizarPagamentoQuandoCupomNaoExiste() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Equador");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Esportes");
        entityManager.persist(categoria);

        Autor autor = new Autor("Diego Santos", "diego@email.com", "Jornalista esportivo");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "História do Futebol",
            "Resumo de esportes",
            "Sumário de futebol",
            new BigDecimal("70.00"),
            220,
            "ISBN-9999999999",
            LocalDateTime.now().plusDays(14),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com cupom inexistente
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador8@email.com",
            "Gustavo",
            "Rocha",
            "24441116055",
            "Calle Principal, 800",
            "Casa 2",
            "Quito",
            pais.getId(),
            null,
            "11988888888",
            "88888888",
            "CUPOM_INEXISTENTE",  // Cupom que não existe
            new CarrinhoDeCompraRequest(
                new BigDecimal("70.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens.length()").value(1))
            .andExpect(jsonPath("$.mensagens[0]").value("Cupom não encontrado"));
    }

    @Test
    void naoDeveRealizarPagamentoQuandoDocumentoInvalido() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Bolívia");
        entityManager.persist(pais);

        Categoria categoria = new Categoria("Música");
        entityManager.persist(categoria);

        Autor autor = new Autor("Amanda Silva", "amanda@email.com", "Musicista");
        entityManager.persist(autor);

        Livro livro = new Livro(
            "Teoria Musical",
            "Resumo de música",
            "Sumário de teoria",
            new BigDecimal("55.00"),
            190,
            "ISBN-0000000000",
            LocalDateTime.now().plusDays(6),
            categoria,
            autor
        );
        entityManager.persist(livro);

        entityManager.flush();
        entityManager.clear();

        // Criar request com documento inválido (não é CPF nem CNPJ válido)
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador9@email.com",
            "Thiago",
            "Barbosa",
            "12345",  // Documento inválido
            "Av Central, 900",
            "Sala 3",
            "La Paz",
            pais.getId(),
            null,
            "11977777777",
            "99999999",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("55.00"),
                List.of(new ItemCarrinhoRequest(livro.getId(), 1))
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens").isArray());
    }

    @Test
    void naoDeveRealizarPagamentoQuandoCarrinhoVazio() throws Exception {
        // Preparar dados
        Pais pais = new Pais("Paraguai");
        entityManager.persist(pais);

        entityManager.flush();
        entityManager.clear();

        // Criar request com carrinho vazio
        RealizaPagamentoRequest request = new RealizaPagamentoRequest(
            "comprador10@email.com",
            "Mariana",
            "Campos",
            "24441116055",
            "Rua do Comércio, 1000",
            "Loja 1",
            "Assunção",
            pais.getId(),
            null,
            "11966666666",
            "10101010",
            null,
            new CarrinhoDeCompraRequest(
                new BigDecimal("0.00"),
                List.of()  // Lista vazia
            )
        );

        // Executar e verificar erro de validação
        mockMvc.perform(post("/api/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.mensagens").isArray());
    }
}

