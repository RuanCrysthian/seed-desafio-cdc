package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.PedidoItem;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.livro.Livro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class ItemCarrinhoRequest {

    @NotNull
    @EntidadeExiste(message = "Livro não encontrado", nomeTabela = Livro.class, nomeCampo = "id")
    private UUID idLivro;

    @NotNull
    @Positive
    private Integer quantidade;

    public PedidoItem toModel(EntityManager entityManager) {
        Livro livro = entityManager.find(Livro.class, this.idLivro);
        if (livro == null) {
            throw new EntityNotFoundException("Livro não encontrado com id: " + this.idLivro);
        }
        PedidoItem pedidoItem = new PedidoItem(livro, this.quantidade);
        entityManager.persist(pedidoItem);
        return pedidoItem;
    }
}
