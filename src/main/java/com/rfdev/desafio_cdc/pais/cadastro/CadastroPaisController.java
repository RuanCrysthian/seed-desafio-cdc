package com.rfdev.desafio_cdc.pais.cadastro;

import com.rfdev.desafio_cdc.pais.Pais;
import com.rfdev.desafio_cdc.pais.PaisRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroPaisController {

    private final PaisRepository paisRepository;

    public CadastroPaisController(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @PostMapping("/api/paises")
    public ResponseEntity<CadastroPaisResponse> cadastrar(@RequestBody @Valid CadastroPaisRequest request) {
        Pais pais = request.toModel();
        paisRepository.save(pais);
        return ResponseEntity.ok(CadastroPaisResponse.of(pais));
    }
}
