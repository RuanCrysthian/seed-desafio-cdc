package com.rfdev.desafio_cdc.livro.listagem;

import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.livro.LivroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListaLivroController {

    private final LivroRepository livroRepository;

    public ListaLivroController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @GetMapping("api/livros")
    public ResponseEntity<ListaLivroResponse> listarLivros(ListaLivroRequest request) {
        List<Livro> livros = livroRepository.findAll();

        return ResponseEntity.ok(ListaLivroResponse.of(livros));
    }
}
