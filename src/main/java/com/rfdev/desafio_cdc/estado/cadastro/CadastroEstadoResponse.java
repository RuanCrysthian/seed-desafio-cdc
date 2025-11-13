package com.rfdev.desafio_cdc.estado.cadastro;


import com.rfdev.desafio_cdc.estado.Estado;

import java.util.UUID;

public record CadastroEstadoResponse(
    UUID id,
    String nome,
    String nomePais
) {

    public static CadastroEstadoResponse of(Estado estado) {
        return new CadastroEstadoResponse(
            estado.getId(),
            estado.getNome(),
            estado.getPais().getNome()
        );
    }
}
