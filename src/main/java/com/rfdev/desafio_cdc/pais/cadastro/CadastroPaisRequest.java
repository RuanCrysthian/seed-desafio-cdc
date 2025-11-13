package com.rfdev.desafio_cdc.pais.cadastro;

import com.rfdev.desafio_cdc.config.CampoUnico;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.validation.constraints.NotBlank;

public record CadastroPaisRequest(
    @NotBlank @CampoUnico(message = "Já existe um pais cadastrado com esse nome", nomeTabela = Pais.class, nomeCampo = "nome") String nome
) {

    public Pais toModel() {
        return new Pais(nome);
    }
}
