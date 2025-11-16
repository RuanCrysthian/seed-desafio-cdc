package com.rfdev.desafio_cdc.cupom.cadastro;

import com.rfdev.desafio_cdc.cupom.Cupom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroCupomController {

    @PersistenceContext
    private EntityManager entityManager;

    public CadastroCupomController() {
    }

    @PostMapping("api/cupons")
    @Transactional
    public ResponseEntity<CadastroCupomResponse> cadastrar(@RequestBody @Valid CadastroCupomRequest request) {
        Cupom cupom = request.toModel();
        entityManager.persist(cupom);
        return ResponseEntity.ok(CadastroCupomResponse.of(cupom));
    }
}
