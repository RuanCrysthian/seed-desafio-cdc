package com.rfdev.desafio_cdc.cupom.cadastro;

import com.rfdev.desafio_cdc.cupom.Cupom;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record CadastroCupomResponse(
    String codigo,
    BigInteger percentualDesconto,
    LocalDateTime dataValidade
) {

    public static CadastroCupomResponse of(Cupom cupom) {
        return new CadastroCupomResponse(
            cupom.getCodigo(),
            cupom.getPercentualDesconto(),
            cupom.getDataValidade()
        );
    }
}
