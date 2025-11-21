package com.rfdev.desafio_cdc.livro.cadastro;

import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.livro.LivroRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroLivroController {

    private final LivroRepository livroRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public CadastroLivroController(
        LivroRepository livroRepository,
        EntityManager entityManager) {
        this.livroRepository = livroRepository;
        this.entityManager = entityManager;
    }

    @PostMapping("/api/livros")
    public ResponseEntity<CadastroLivroResponse> cadastrar(@RequestBody @Valid CadastroLivroRequest request) {

        Livro livro = request.toModel(entityManager);
        livroRepository.save(livro);

        return ResponseEntity.ok(CadastroLivroResponse.of(livro));
    }
}
