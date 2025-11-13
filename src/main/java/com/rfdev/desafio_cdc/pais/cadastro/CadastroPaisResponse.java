package com.rfdev.desafio_cdc.pais.cadastro;

import com.rfdev.desafio_cdc.pais.Pais;

import java.util.UUID;

public record CadastroPaisResponse(
    UUID id,
    String nome
) {

    public static CadastroPaisResponse of(Pais pais) {
        return new CadastroPaisResponse(pais.getId(), pais.getNome());
    }
}
