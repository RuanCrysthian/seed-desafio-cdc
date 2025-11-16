package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.Compra;

import java.math.BigDecimal;
import java.util.List;

public record RealizaPagamentoResponse(
    String emailComprador,
    BigDecimal valorPedido,
    String codigoCupom,
    BigDecimal valorFinal,
    List<ItemCompraResponse> itensComprados) {

    public static RealizaPagamentoResponse of(Compra compra) {
        return new RealizaPagamentoResponse(
            compra.getEmail(),
            compra.getPedido().calcularTotal(),
            compra.getCupom() != null ? compra.getCupom().getCodigo() : "N/A",
            compra.calcularValorTotal(),
            compra.getPedido().getItens().stream().map(ItemCompraResponse::of).toList());
    }
}
