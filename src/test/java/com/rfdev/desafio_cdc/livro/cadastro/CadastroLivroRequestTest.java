package com.rfdev.desafio_cdc.livro.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.livro.Livro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CadastroLivroRequestTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CadastroLivroRequest cadastroLivroRequest;

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoExistir() {
        Mockito.when(entityManager.find(Mockito.eq(Categoria.class), Mockito.any()))
            .thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            cadastroLivroRequest.toModel(entityManager);
        });
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoExistir() {
        Categoria categoria = new Categoria("Categoria Teste");
        Mockito.when(entityManager.find(Mockito.eq(Categoria.class), Mockito.any()))
            .thenReturn(categoria);
        Mockito.when(entityManager.find(Mockito.eq(Autor.class), Mockito.any()))
            .thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            cadastroLivroRequest.toModel(entityManager);
        });
    }

    @Test
    void deveConverterParaModeloQuandoCategoriaEAutorExistirem() {
        Categoria categoria = new Categoria("Categoria Teste");
        Autor autor = new Autor("Autor Teste", "autor@emial.com", "Descricao do autor");
        Mockito.when(entityManager.find(Mockito.eq(Categoria.class), Mockito.any()))
            .thenReturn(categoria);
        Mockito.when(entityManager.find(Mockito.eq(Autor.class), Mockito.any()))
            .thenReturn(autor);

        CadastroLivroRequest request = new CadastroLivroRequest(
            "Titulo Teste",
            "Resumo do livro",
            "Sumario do livro",
            new java.math.BigDecimal("25.00"),
            150,
            "ISBN-1234567890",
            java.time.LocalDateTime.now().plusDays(1),
            UUID.randomUUID(),
            UUID.randomUUID()
        );

        Livro livro = request.toModel(entityManager);
        Assertions.assertNotNull(livro);
        Assertions.assertEquals("Titulo Teste", livro.getTitulo());
        Assertions.assertEquals(categoria, livro.getCategoria());
        Assertions.assertEquals(autor, livro.getAutor());
        Assertions.assertEquals(request.numeroPaginas(), livro.getNumeroPaginas());
        
    }

}