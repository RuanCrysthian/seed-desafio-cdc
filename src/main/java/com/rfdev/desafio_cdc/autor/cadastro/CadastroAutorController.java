package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroAutorController {

    @PersistenceContext
    private EntityManager entityManager;

    public CadastroAutorController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @PostMapping("api/autores")
    @Transactional
    public ResponseEntity<CadastroAutorResponse> cadastro(@RequestBody @Valid CadastroAutorRequest request) {
        Autor autor = request.toModel();
        entityManager.persist(autor);
        return ResponseEntity.ok(CadastroAutorResponse.of(autor));
    }
}
