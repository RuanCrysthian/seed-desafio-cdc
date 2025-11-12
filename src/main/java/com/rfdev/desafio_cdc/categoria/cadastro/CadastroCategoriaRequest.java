package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.Categoria;

public record CadastroCategoriaRequest(
    String nome
) {

    public Categoria toModel() {
        return new Categoria(nome);
    }
}
