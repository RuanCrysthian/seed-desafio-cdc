package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.config.Documento;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.cupom.Cupom;
import com.rfdev.desafio_cdc.cupom.CupomRepository;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;


public record RealizaPagamentoRequest(

    @NotBlank
    @Email
    String email,

    @NotBlank
    String nome,

    @NotBlank
    String sobrenome,

    @NotBlank
    @Documento
    String documento,

    @NotBlank
    String endereco,

    @NotBlank
    String complemento,

    @NotBlank
    String cidade,

    @NotNull
    @EntidadeExiste(message = "Pais não encontrado", nomeTabela = Pais.class, nomeCampo = "id")
    UUID paisId,

    @EntidadeExiste(message = "Estado não encontrado", nomeTabela = Estado.class, nomeCampo = "id")
    UUID estadoId,

    @NotBlank
    String telefone,

    @NotBlank
    String cep,

    @EntidadeExiste(message = "Cupom não encontrado", nomeTabela = Cupom.class, nomeCampo = "codigo")
    String cupomCodigo,

    @NotNull
    @Valid
    CarrinhoDeCompraRequest carrinho
) {
    public Compra toModel(EntityManager entityManager, CupomRepository cupomRepository) {
        Pais pais = entityManager.find(Pais.class, this.paisId);
        if (pais == null) {
            throw new EntityNotFoundException("Pais não encontrado");
        }
        Estado estado = this.estadoId != null ? entityManager.find(Estado.class, this.estadoId) : null;
        if (pais.possuiEstados() && estado == null) {
            throw new EntityNotFoundException("Estado obrigatório para o país selecionado");
        }

        Optional<Cupom> cupom = cupomRepository.findByCodigo(this.cupomCodigo);

        Compra compra = new Compra(
            this.email,
            this.nome,
            this.sobrenome,
            this.documento,
            this.endereco,
            this.complemento,
            this.cidade,
            pais,
            estado,
            this.telefone,
            this.cep,
            this.carrinho.toModel(entityManager)
        );

        if (cupom.isPresent() && cupom.get().estaValido()) {
            compra.aplicarCupom(cupom.get());
        }
        return compra;
    }
}
