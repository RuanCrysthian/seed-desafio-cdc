package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.config.CampoUnico;
import jakarta.validation.constraints.NotBlank;

public record CadastroCategoriaRequest(
    @NotBlank @CampoUnico(message = "Nome já cadastrado", nomeTabela = Categoria.class, nomeCampo = "nome") String nome
) {

    public Categoria toModel() {
        return new Categoria(nome);
    }
}
