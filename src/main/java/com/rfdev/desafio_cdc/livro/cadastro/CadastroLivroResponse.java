package com.rfdev.desafio_cdc.livro.cadastro;

import com.rfdev.desafio_cdc.livro.Livro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CadastroLivroResponse(
    String id,
    String titulo,
    String resumo,
    String sumario,
    BigDecimal preco,
    Integer numeroPaginas,
    String isbn,
    LocalDateTime dataPublicacao,
    String categoria,
    String autor
) {

    public static CadastroLivroResponse of(Livro livro) {
        return new CadastroLivroResponse(
            livro.getId().toString(),
            livro.getTitulo(),
            livro.getResumo(),
            livro.getSumario(),
            livro.getPreco(),
            livro.getNumeroPaginas(),
            livro.getIsbn(),
            livro.getDataPublicacao(),
            livro.getCategoria().getNome(),
            livro.getAutor().getNome()
        );
    }
}
