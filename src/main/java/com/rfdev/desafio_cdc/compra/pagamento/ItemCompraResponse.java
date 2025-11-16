package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.PedidoItem;

import java.math.BigDecimal;

public record ItemCompraResponse(
    String tituloLivro,
    Integer quantidade,
    BigDecimal precoUnitario
) {

    public static ItemCompraResponse of(PedidoItem pedidoItem) {
        return new ItemCompraResponse(
            pedidoItem.getLivro().getTitulo(),
            pedidoItem.getQuantidade(),
            pedidoItem.getLivro().getPreco()
        );
    }
}
