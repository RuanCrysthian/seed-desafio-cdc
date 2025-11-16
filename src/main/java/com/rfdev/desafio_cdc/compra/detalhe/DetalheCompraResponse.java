package com.rfdev.desafio_cdc.compra.detalhe;

import com.rfdev.desafio_cdc.compra.Compra;

import java.math.BigDecimal;
import java.util.List;

public record DetalheCompraResponse(
    String email,
    String nomeCompleto,
    String endereco,
    String complemento,
    String cidade,
    String pais,
    String estado,
    String telefone,
    String cep,
    BigDecimal valorOriginal,
    Boolean possuiCupomDeDesconto,
    BigDecimal valorTotal,
    List<ItemCompradoResponse> itensComprados
) {

    public static DetalheCompraResponse of(Compra compra) {
        return new DetalheCompraResponse(
            compra.getEmail(),
            compra.getNome() + " " + compra.getSobrenome(),
            compra.getEndereco(),
            compra.getComplemento(),
            compra.getCidade(),
            compra.getPais().getNome(),
            compra.getEstado() != null ? compra.getEstado().getNome() : "N/A",
            compra.getTelefone(),
            compra.getCep(),
            compra.getPedido().calcularTotal(),
            compra.getCupom() != null,
            compra.calcularValorTotal(),
            compra.getPedido().getItens().stream()
                .map(item -> new ItemCompradoResponse(
                    item.getLivro().getTitulo(),
                    item.getLivro().getAutor().getNome(),
                    item.getQuantidade(),
                    item.getLivro().getPreco()
                ))
                .toList()
        );
    }
}
