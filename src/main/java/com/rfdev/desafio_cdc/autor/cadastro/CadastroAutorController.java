package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.autor.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroAutorController {

    private final AutorRepository autorRepository;

    public CadastroAutorController(AutorRepository usuarioRepository) {
        this.autorRepository = usuarioRepository;
    }

    @PostMapping("api/autores")
    @Transactional
    public ResponseEntity<CadastroAutorResponse> cadastro(@RequestBody @Valid CadastroAutorRequest request) {
        Autor autor = request.toModel();
        autorRepository.save(autor);
        return ResponseEntity.ok(CadastroAutorResponse.of(autor));
    }
}
