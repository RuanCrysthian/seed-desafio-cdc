package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.compra.CompraRepository;
import com.rfdev.desafio_cdc.config.EstadoObrigatorioParaPaisValidator;
import com.rfdev.desafio_cdc.config.EstadoPertenceAoPaisValidator;
import com.rfdev.desafio_cdc.config.TotalCompraValidator;
import com.rfdev.desafio_cdc.cupom.CupomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealizaPagamentoController {

    private final EstadoObrigatorioParaPaisValidator estadoObrigatorioParaPaisValidator;
    private final EstadoPertenceAoPaisValidator estadoPertendeAoPaisValidator;
    private final TotalCompraValidator totalCompraValidator;
    private final CompraRepository compraRepository;
    private final CupomRepository cupomRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RealizaPagamentoController(
        EstadoObrigatorioParaPaisValidator estadoObrigatorioParaPaisValidator,
        EstadoPertenceAoPaisValidator estadoPertendeAoPaisValidator,
        TotalCompraValidator totalCompraValidator,
        CompraRepository compraRepository,
        CupomRepository cupomRepository) {
        this.estadoObrigatorioParaPaisValidator = estadoObrigatorioParaPaisValidator;
        this.estadoPertendeAoPaisValidator = estadoPertendeAoPaisValidator;
        this.totalCompraValidator = totalCompraValidator;
        this.compraRepository = compraRepository;
        this.cupomRepository = cupomRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(estadoObrigatorioParaPaisValidator, estadoPertendeAoPaisValidator, totalCompraValidator);
    }

    @PostMapping("/api/pagamentos")
    @Transactional
    public ResponseEntity<RealizaPagamentoResponse> realizaPagamento(@RequestBody @Valid RealizaPagamentoRequest request) {
        System.out.printf(request.toString());

        Compra compra = request.toModel(entityManager, cupomRepository);

        compraRepository.save(compra);

        return ResponseEntity.ok(RealizaPagamentoResponse.of(compra));
    }
}
