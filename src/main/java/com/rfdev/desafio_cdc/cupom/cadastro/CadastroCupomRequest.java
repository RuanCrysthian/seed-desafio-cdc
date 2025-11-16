package com.rfdev.desafio_cdc.cupom.cadastro;

import com.rfdev.desafio_cdc.config.CampoUnico;
import com.rfdev.desafio_cdc.cupom.Cupom;
import jakarta.validation.constraints.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record CadastroCupomRequest(
    @NotBlank @CampoUnico(message = "Já existe um cupom com esse código", nomeTabela = Cupom.class, nomeCampo = "codigo") String codigo,
    @NotNull @Positive @Min(1) @Max(99) BigInteger percentualDesconto,
    @NotNull @Future LocalDateTime dataValidade
) {
    public Cupom toModel() {
        return new Cupom(codigo, percentualDesconto, dataValidade);
    }
}
