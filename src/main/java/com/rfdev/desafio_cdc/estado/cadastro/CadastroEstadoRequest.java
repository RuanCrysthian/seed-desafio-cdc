package com.rfdev.desafio_cdc.estado.cadastro;

import com.rfdev.desafio_cdc.config.CampoUnico;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CadastroEstadoRequest(
    @NotBlank @CampoUnico(message = "Já existe um estado com esse nome", nomeTabela = Estado.class, nomeCampo = "nome") String nome,
    @NotNull @EntidadeExiste(message = "Pais não encontrado", nomeTabela = Pais.class, nomeCampo = "id") UUID paisId
) {

    public Estado toModel(EntityManager entityManager) {
        Pais pais = entityManager.find(Pais.class, paisId);
        if (pais == null) {
            throw new EntityNotFoundException("Pais não encontrado");
        }
        Estado estado = new Estado(nome, pais);
        pais.adicionarEstado(estado);
        return estado;
    }
}
