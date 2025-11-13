package com.rfdev.desafio_cdc.livro.detalhe;

import com.rfdev.desafio_cdc.livro.Livro;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DetalheLivroResponse(
    String titulo,
    String resumo,
    String sumario,
    BigDecimal preco,
    Integer numeroPaginas,
    String isbn,
    LocalDateTime dataPublicacao,
    DetalheLivroAutorResponse autor
) {

    public static DetalheLivroResponse of(Livro livro) {
        return new DetalheLivroResponse(
            livro.getTitulo(),
            livro.getResumo(),
            livro.getSumario(),
            livro.getPreco(),
            livro.getNumeroPaginas(),
            livro.getIsbn(),
            livro.getDataPublicacao(),
            DetalheLivroAutorResponse.of(livro.getAutor())
        );
    }

}
