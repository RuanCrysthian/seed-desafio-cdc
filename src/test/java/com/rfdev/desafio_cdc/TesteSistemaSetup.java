package com.rfdev.desafio_cdc;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public abstract class TesteSistemaSetup {

    static {
        DatabaseTest.isRunning();
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @AfterEach
    void cleanDatabase() {
        entityManager.createNativeQuery("DELETE FROM pedidos_itens").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM compras").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM pedidos").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM pedido_itens").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM cupons").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM livros").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM autores").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM categorias").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM estados").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM paises").executeUpdate();
        entityManager.flush();
    }
}
