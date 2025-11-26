package com.rfdev.desafio_cdc.pais.cadastro;

import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroPaisController {

    @PersistenceContext
    private final EntityManager entityManager;

    public CadastroPaisController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/api/paises")
    public ResponseEntity<CadastroPaisResponse> cadastrar(@RequestBody @Valid CadastroPaisRequest request) {
        Pais pais = request.toModel();
        entityManager.persist(pais);
        return ResponseEntity.ok(CadastroPaisResponse.of(pais));
    }
}
