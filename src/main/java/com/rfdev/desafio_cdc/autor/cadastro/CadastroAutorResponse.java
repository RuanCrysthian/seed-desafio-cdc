package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public record CadastroAutorResponse(
    String id,
    String nome,
    String email,
    String descricao,
    LocalDateTime dataCriacao
) {
    public static CadastroAutorResponse of(@Valid Autor autor) {
        return new CadastroAutorResponse(
            autor.getId().toString(),
            autor.getNome(),
            autor.getEmail(),
            autor.getDescricao(),
            autor.getDataCriacao()
        );
    }
}
