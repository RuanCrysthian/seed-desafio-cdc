package com.rfdev.desafio_cdc.livro.detalhe;

import com.rfdev.desafio_cdc.autor.Autor;

public record DetalheLivroAutorResponse(
    String nome,
    String descricao
) {

    public static DetalheLivroAutorResponse of(Autor autor) {
        return new DetalheLivroAutorResponse(autor.getNome(), autor.getDescricao());
    }
}
