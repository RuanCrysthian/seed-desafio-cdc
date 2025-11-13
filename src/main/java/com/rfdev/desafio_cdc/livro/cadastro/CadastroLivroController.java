package com.rfdev.desafio_cdc.livro.cadastro;

import com.rfdev.desafio_cdc.autor.AutorRepository;
import com.rfdev.desafio_cdc.categoria.CategoriaRepository;
import com.rfdev.desafio_cdc.livro.Livro;
import com.rfdev.desafio_cdc.livro.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroLivroController {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public CadastroLivroController(
        LivroRepository livroRepository,
        AutorRepository autorRepository,
        CategoriaRepository categoriaRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("/api/livros")
    public ResponseEntity<CadastroLivroResponse> cadastrar(@RequestBody @Valid CadastroLivroRequest request) {

        Livro livro = request.toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        return ResponseEntity.ok(CadastroLivroResponse.of(livro));
    }
}
