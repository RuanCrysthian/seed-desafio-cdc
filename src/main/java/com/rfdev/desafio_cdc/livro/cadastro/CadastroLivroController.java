package com.rfdev.desafio_cdc.livro.cadastro;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rfdev.desafio_cdc.livro.Livro;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

@RestController
public class CadastroLivroController {

    @PersistenceContext
    private EntityManager entityManager;

    public CadastroLivroController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/api/livros")
    @Transactional
    public ResponseEntity<CadastroLivroResponse> cadastrar(@RequestBody @Valid CadastroLivroRequest request) {

        Livro livro = request.toModel(entityManager);
        entityManager.persist(livro);

        return ResponseEntity.ok(CadastroLivroResponse.of(livro));
    }
}
