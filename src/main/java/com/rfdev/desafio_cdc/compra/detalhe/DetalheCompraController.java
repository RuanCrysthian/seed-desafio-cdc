package com.rfdev.desafio_cdc.compra.detalhe;

import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.compra.CompraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DetalheCompraController {

    private final CompraRepository compraRepository;

    public DetalheCompraController(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @GetMapping("api/compras/{id}")
    public ResponseEntity<DetalheCompraResponse> detalharCompra(@PathVariable UUID id) {

        Compra compra = compraRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Compra não encontrada"));
        return ResponseEntity.ok(DetalheCompraResponse.of(compra));

    }
}
