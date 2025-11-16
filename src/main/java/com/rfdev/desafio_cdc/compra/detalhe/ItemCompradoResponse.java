package com.rfdev.desafio_cdc.compra.detalhe;

import java.math.BigDecimal;

public record ItemCompradoResponse(
    String tituloLivro,
    String autor,
    Integer quantidade,
    BigDecimal precoUnitario
) {
}
