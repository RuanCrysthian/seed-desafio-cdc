package com.rfdev.desafio_cdc.livro.listagem;

import com.rfdev.desafio_cdc.livro.Livro;

import java.util.List;

public record ListaLivroResponse(
    List<LivroResumo> livros
) {

    public static ListaLivroResponse of(List<Livro> livros) {
        List<LivroResumo> livrosResumo = livros.stream()
            .map(livro -> new LivroResumo(livro.getId().toString(), livro.getTitulo()))
            .toList();
        return new ListaLivroResponse(livrosResumo);
    }

    public record LivroResumo(
        String id,
        String titulo
    ) {
    }
}


