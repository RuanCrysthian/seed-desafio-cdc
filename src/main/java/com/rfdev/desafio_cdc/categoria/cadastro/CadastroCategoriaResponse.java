package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.Categoria;

public record CadastroCategoriaResponse(
    String id,
    String nome
) {

    public static CadastroCategoriaResponse of(Categoria categoria) {
        return new CadastroCategoriaResponse(
            categoria.getId().toString(),
            categoria.getNome()
        );
    }
}
