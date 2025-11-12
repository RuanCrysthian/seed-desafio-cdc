package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.autor.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroAutorController {

    private final AutorRepository autorRepository;
    private final ProibeEmailDuplicadoAutorValidor proibeEmailDuplicadoAutorValidor;

    public CadastroAutorController(
        AutorRepository usuarioRepository,
        ProibeEmailDuplicadoAutorValidor proibeEmailDuplicadoAutorValidor) {
        this.autorRepository = usuarioRepository;
        this.proibeEmailDuplicadoAutorValidor = proibeEmailDuplicadoAutorValidor;
    }

    @InitBinder
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        binder.addValidators(proibeEmailDuplicadoAutorValidor);
    }

    @PostMapping("api/autores")
    @Transactional
    public ResponseEntity<CadastroAutorResponse> cadastro(@RequestBody @Valid CadastroAutorRequest request) {
        Autor autor = request.toModel();
        autorRepository.save(autor);
        return ResponseEntity.ok(CadastroAutorResponse.of(autor));
    }
}
