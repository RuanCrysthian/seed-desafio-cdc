package com.rfdev.desafio_cdc.compra;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Positive
    private BigDecimal total;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoItem> itens;

    @Deprecated
    public Pedido() {
    }

    public Pedido(@NotNull @Positive BigDecimal total, @NotNull List<PedidoItem> itens) {
        this.total = total;
        this.itens = itens;
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
            .map(PedidoItem::totalItem)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
