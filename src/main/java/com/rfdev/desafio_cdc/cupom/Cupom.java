package com.rfdev.desafio_cdc.cupom;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cupons")
@Getter
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cupom_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "codigo", unique = true, nullable = false)
    @NotBlank
    private String codigo;

    @Min(1)
    @Max(99)
    @Positive
    @Column(name = "percentual_desconto", nullable = false)
    private BigInteger percentualDesconto;

    @Future
    @Column(name = "data_validade", nullable = false)
    private LocalDateTime dataValidade;

    @Deprecated
    public Cupom() {
    }

    public Cupom(
        @NotBlank String codigo,
        @Positive @Min(1) @Max(99) BigInteger percentualDesconto,
        @NotNull @Future LocalDateTime dataValidade) {
        this.codigo = codigo;
        this.percentualDesconto = percentualDesconto;
        this.dataValidade = dataValidade;
    }
}
