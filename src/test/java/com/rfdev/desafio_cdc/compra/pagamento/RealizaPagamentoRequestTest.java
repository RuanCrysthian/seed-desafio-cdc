package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.cupom.Cupom;
import com.rfdev.desafio_cdc.cupom.CupomRepository;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RealizaPagamentoRequestTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CupomRepository cupomRepository;

    @InjectMocks
    private RealizaPagamentoRequest realizaPagamentoRequest;

    @Test
    void deveLancarExcecaoQuandoPaisNaoExistir() {
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            realizaPagamentoRequest.toModel(entityManager, cupomRepository);
        });
    }

    @Test
    void deveLancarExcecaoQuandoPaisPossuirEstadosEEstadoNaoExistir() {
        Pais pais = new Pais("Pais Teste");
        Estado estado = new Estado("Estado Teste", pais);
        pais.adicionarEstado(estado);

        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);
        Mockito.when(entityManager.find(Mockito.eq(Estado.class), Mockito.any()))
            .thenReturn(null);

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            UUID.randomUUID(),
            "11999999999",
            "12345000",
            null,
            new CarrinhoDeCompraRequest(new BigDecimal("1000.00"), List.of(new ItemCarrinhoRequest(UUID.randomUUID(), 1)))
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            realizaPagamentoRequest.toModel(entityManager, cupomRepository);
        });
    }

    @Test
    void deveLancarExcecaoQuandoPaisPossuirEstadosEEstadoIdForNulo() {
        Pais pais = new Pais("Pais Teste");
        Estado estado = new Estado("Estado Teste", pais);
        pais.adicionarEstado(estado);

        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            null,
            "11999999999",
            "12345000",
            null,
            new CarrinhoDeCompraRequest(new BigDecimal("1000.00"), List.of(new ItemCarrinhoRequest(UUID.randomUUID(), 1)))
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            realizaPagamentoRequest.toModel(entityManager, cupomRepository);
        });
    }

    @Test
    void deveLancarExcecaoQuandoPaisNaoPossuirEstadosEEstadoIdForInformado() {
        Pais pais = new Pais("Pais Teste");
        Pais paisSemEstado = new Pais("Pais Sem Estado");
        Estado estado = new Estado("Estado Teste", pais);

        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(paisSemEstado);
        Mockito.when(entityManager.find(Mockito.eq(Estado.class), Mockito.any()))
            .thenReturn(estado);

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            paisSemEstado.getId(),
            UUID.randomUUID(),
            "11999999999",
            "12345000",
            null,
            new CarrinhoDeCompraRequest(new BigDecimal("1000.00"), List.of(new ItemCarrinhoRequest(UUID.randomUUID(), 1)))
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            realizaPagamentoRequest.toModel(entityManager, cupomRepository);
        });

    }

    @Test
    void deveNaoAplicarCupomQuandoNaoForInformado() {
        Pais pais = new Pais("Pais Teste");
        Categoria categoria = new Categoria("Categoria Teste");
        Autor autor = new Autor("Autor Teste", "autor.teste@emial.com", "Descricao do autor");
        Livro livro = new Livro(
            "Titulo Teste",
            "Resumo do livro",
            "Sumario do livro",
            new BigDecimal("1000.00"),
            150,
            "ISBN-1234567890",
            java.time.LocalDateTime.now().plusDays(1),
            categoria,
            autor
        );
        int quantidadeLivros = 1;
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(java.util.Optional.empty());
        Mockito.when(entityManager.find(Mockito.eq(Livro.class), Mockito.any()))
            .thenReturn(livro);

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            null,
            "11999999999",
            "12345000",
            "12345",
            new CarrinhoDeCompraRequest(
                new BigDecimal("1000.00"),
                List.of(new ItemCarrinhoRequest(UUID.randomUUID(), quantidadeLivros)))
        );

        Compra compra = realizaPagamentoRequest.toModel(entityManager, cupomRepository);

        Assertions.assertNull(compra.getCupom());
        Assertions.assertEquals(livro.getPreco().multiply(new BigDecimal(quantidadeLivros)), compra.calcularValorTotal());
    }

    @Test
    void deveNaoAplicarCupomQuandoEstiverVencido() {
        Pais pais = new Pais("Pais Teste");
        Categoria categoria = new Categoria("Categoria Teste");
        Autor autor = new Autor("Autor Teste", "autor.teste@emial.com", "Descricao do autor");
        Livro livro = new Livro(
            "Titulo Teste",
            "Resumo do livro",
            "Sumario do livro",
            new BigDecimal("1000.00"),
            150,
            "ISBN-1234567890",
            java.time.LocalDateTime.now().plusDays(1),
            categoria,
            autor
        );
        Cupom cupom = new Cupom("12345", new BigInteger("50"), java.time.LocalDateTime.now().minusDays(10));
        int quantidadeLivros = 1;
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(java.util.Optional.empty());
        Mockito.when(entityManager.find(Mockito.eq(Livro.class), Mockito.any()))
            .thenReturn(livro);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(Optional.of(cupom));

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            null,
            "11999999999",
            "12345000",
            "12345",
            new CarrinhoDeCompraRequest(
                new BigDecimal("1000.00"),
                List.of(new ItemCarrinhoRequest(UUID.randomUUID(), quantidadeLivros)))
        );

        Compra compra = realizaPagamentoRequest.toModel(entityManager, cupomRepository);

        Assertions.assertNull(compra.getCupom());
        Assertions.assertEquals(livro.getPreco().multiply(new BigDecimal(quantidadeLivros)), compra.calcularValorTotal());
    }

    @Test
    void deveAplicarCupomQuandoForInformado() {
        Pais pais = new Pais("Pais Teste");
        Categoria categoria = new Categoria("Categoria Teste");
        Autor autor = new Autor("Autor Teste", "autor.teste@emial.com", "Descricao do autor");
        Livro livro = new Livro(
            "Titulo Teste",
            "Resumo do livro",
            "Sumario do livro",
            new BigDecimal("1000.00"),
            150,
            "ISBN-1234567890",
            java.time.LocalDateTime.now().plusDays(1),
            categoria,
            autor
        );
        Cupom cupom = new Cupom("12345", new BigInteger("50"), java.time.LocalDateTime.now().plusDays(10));
        int quantidadeLivros = 1;
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(java.util.Optional.empty());
        Mockito.when(entityManager.find(Mockito.eq(Livro.class), Mockito.any()))
            .thenReturn(livro);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(Optional.of(cupom));

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            null,
            "11999999999",
            "12345000",
            "12345",
            new CarrinhoDeCompraRequest(
                new BigDecimal("1000.00"),
                List.of(new ItemCarrinhoRequest(UUID.randomUUID(), quantidadeLivros)))
        );

        Compra compra = realizaPagamentoRequest.toModel(entityManager, cupomRepository);

        Assertions.assertNotNull(compra.getCupom());
        Assertions.assertEquals(
            livro.getPreco().multiply(new BigDecimal(quantidadeLivros)).divide(new BigDecimal("2"), RoundingMode.CEILING),
            compra.calcularValorTotal());
    }

    @Test
    void deveLancarExcecaoQuandoTentarAplicarCupomJaAplicado() {
        Pais pais = new Pais("Pais Teste");
        Categoria categoria = new Categoria("Categoria Teste");
        Autor autor = new Autor("Autor Teste", "autor.teste@emial.com", "Descricao do autor");
        Livro livro = new Livro(
            "Titulo Teste",
            "Resumo do livro",
            "Sumario do livro",
            new BigDecimal("1000.00"),
            150,
            "ISBN-1234567890",
            java.time.LocalDateTime.now().plusDays(1),
            categoria,
            autor
        );
        Cupom cupom = new Cupom("12345", new BigInteger("50"), java.time.LocalDateTime.now().plusDays(10));
        int quantidadeLivros = 1;
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(java.util.Optional.empty());
        Mockito.when(entityManager.find(Mockito.eq(Livro.class), Mockito.any()))
            .thenReturn(livro);
        Mockito.when(cupomRepository.findByCodigo("12345"))
            .thenReturn(Optional.of(cupom));

        realizaPagamentoRequest = new RealizaPagamentoRequest(
            "email@email.com",
            "Nome",
            "Sobrenome",
            "12345678900",
            "Endereco",
            "Complemento",
            "Cidade",
            pais.getId(),
            null,
            "11999999999",
            "12345000",
            "12345",
            new CarrinhoDeCompraRequest(
                new BigDecimal("1000.00"),
                List.of(new ItemCarrinhoRequest(UUID.randomUUID(), quantidadeLivros)))
        );

        Compra compra = realizaPagamentoRequest.toModel(entityManager, cupomRepository);
        Cupom segundoCupom = new Cupom("CUPOM2", new BigInteger("20"), java.time.LocalDateTime.now().plusDays(10));

        IllegalStateException exception = Assertions.assertThrows(
            IllegalStateException.class,
            () -> compra.aplicarCupom(segundoCupom)
        );

        Assertions.assertEquals("Cupom já foi aplicado nesta compra.", exception.getMessage());
    }

}