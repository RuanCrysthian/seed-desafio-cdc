package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.Pedido;
import com.rfdev.desafio_cdc.compra.PedidoItem;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
public class CarrinhoDeCompraRequest {

    @NotNull
    @Positive
    private BigDecimal total;

    @Size(min = 1)
    @Valid
    private List<ItemCarrinhoRequest> itens;


    public Pedido toModel(EntityManager entityManager) {
        List<PedidoItem> pedidoItens = itens.stream()
            .map(itemRequest -> itemRequest.toModel(entityManager))
            .toList();
        Pedido pedido = new Pedido(total, pedidoItens);
        entityManager.persist(pedido);
        return pedido;
    }
}
