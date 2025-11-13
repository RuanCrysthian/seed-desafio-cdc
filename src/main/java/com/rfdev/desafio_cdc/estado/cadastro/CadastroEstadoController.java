package com.rfdev.desafio_cdc.estado.cadastro;

import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.estado.EstadoRepository;
import com.rfdev.desafio_cdc.pais.PaisRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroEstadoController {

    private final EstadoRepository estadoRepository;
    private final PaisRepository paisRepository;

    public CadastroEstadoController(EstadoRepository estadoRepository, PaisRepository paisRepository) {
        this.estadoRepository = estadoRepository;
        this.paisRepository = paisRepository;
    }

    @PostMapping("/api/estados")
    public ResponseEntity<CadastroEstadoResponse> cadastrar(@RequestBody @Valid CadastroEstadoRequest request) {
        Estado estado = request.toModel(paisRepository);
        estadoRepository.save(estado);
        return ResponseEntity.ok(CadastroEstadoResponse.of(estado));
    }
}
