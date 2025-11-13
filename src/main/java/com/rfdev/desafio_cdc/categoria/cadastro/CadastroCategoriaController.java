package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.categoria.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroCategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CadastroCategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("api/categorias")
    public ResponseEntity<CadastroCategoriaResponse> cadastrar(@RequestBody @Valid CadastroCategoriaRequest request) {
        Categoria categoria = request.toModel();
        categoriaRepository.save(categoria);
        return ResponseEntity.ok(CadastroCategoriaResponse.of(categoria));
    }
}
