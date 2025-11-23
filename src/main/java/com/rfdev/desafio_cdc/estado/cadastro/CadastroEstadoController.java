package com.rfdev.desafio_cdc.estado.cadastro;

import com.rfdev.desafio_cdc.estado.Estado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroEstadoController {

    @PersistenceContext
    private final EntityManager entityManager;

    public CadastroEstadoController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/api/estados")
    @Transactional
    public ResponseEntity<CadastroEstadoResponse> cadastrar(@RequestBody @Valid CadastroEstadoRequest request) {
        Estado estado = request.toModel(entityManager);
        entityManager.persist(estado);
        return ResponseEntity.ok(CadastroEstadoResponse.of(estado));
    }
}
