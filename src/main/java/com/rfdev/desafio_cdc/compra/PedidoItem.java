package com.rfdev.desafio_cdc.compra;

import com.rfdev.desafio_cdc.livro.Livro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pedido_itens")
@Getter
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pedido_item_id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(nullable = false)
    @Positive
    private Integer quantidade;

    @Deprecated
    public PedidoItem() {
    }

    public PedidoItem(@NotNull Livro livro, @Positive Integer quantidade) {
        this.livro = livro;
        this.quantidade = quantidade;
    }

    public BigDecimal totalItem() {
        return livro.getPreco().multiply(new BigDecimal(quantidade));
    }

}
