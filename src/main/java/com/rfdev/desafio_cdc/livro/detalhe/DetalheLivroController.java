package com.rfdev.desafio_cdc.livro.detalhe;

import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.livro.LivroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DetalheLivroController {

    private final LivroRepository livroRepository;

    public DetalheLivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }


    @GetMapping("/api/livros/{livroId}")
    public ResponseEntity<DetalheLivroResponse> detalhar(@PathVariable UUID livroId) {
        Livro livro = livroRepository.findById(livroId)
            .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));

        return ResponseEntity.ok().body(DetalheLivroResponse.of(livro));

    }
}
