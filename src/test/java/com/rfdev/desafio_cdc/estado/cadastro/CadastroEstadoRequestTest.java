package com.rfdev.desafio_cdc.estado.cadastro;

import com.rfdev.desafio_cdc.estado.Estado;
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

@ExtendWith(MockitoExtension.class)
class CadastroEstadoRequestTest {

    @Mock
    EntityManager entityManager;

    @InjectMocks
    CadastroEstadoRequest cadastroEstadoRequest;

    @Test
    void deveLancarExcecaoQuandoPaisNaoExistir() {
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            cadastroEstadoRequest.toModel(entityManager);
        });
    }

    @Test
    void deveConverterParaModeloQuandoPaisExistir() {
        Pais pais = new Pais("Pais Teste");
        Mockito.when(entityManager.find(Mockito.eq(Pais.class), Mockito.any()))
            .thenReturn(pais);

        CadastroEstadoRequest request = new CadastroEstadoRequest(
            "Estado Teste",
            java.util.UUID.randomUUID()
        );

        Estado estado = request.toModel(entityManager);

        Assertions.assertNotNull(estado);
        Assertions.assertEquals("Estado Teste", estado.getNome());
        Assertions.assertEquals(pais, estado.getPais());
    }

}