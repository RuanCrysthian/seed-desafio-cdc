package com.rfdev.desafio_cdc.estado.cadastro;

import com.rfdev.desafio_cdc.config.CampoUnico;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import com.rfdev.desafio_cdc.pais.PaisRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastroEstadoRequest(
    @NotBlank @CampoUnico(message = "Já existe um estado com esse nome", nomeTabela = Estado.class, nomeCampo = "nome") String nome,
    @NotNull @EntidadeExiste(message = "Pais não encontrado", nomeTabela = Pais.class, nomeCampo = "id") UUID paisId
) {

    public Estado toModel(PaisRepository paisRepository) {
        Pais pais = paisRepository.findById(paisId)
            .orElseThrow(() -> new IllegalArgumentException("Pais não encontrado"));
        return new Estado(nome, pais);
    }
}
