package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroCategoriaController {

    @PersistenceContext
    private final EntityManager entityManager;

    public CadastroCategoriaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("api/categorias")
    @Transactional
    public ResponseEntity<CadastroCategoriaResponse> cadastrar(@RequestBody @Valid CadastroCategoriaRequest request) {
        Categoria categoria = request.toModel();
        entityManager.persist(categoria);
        return ResponseEntity.ok(CadastroCategoriaResponse.of(categoria));
    }
}
