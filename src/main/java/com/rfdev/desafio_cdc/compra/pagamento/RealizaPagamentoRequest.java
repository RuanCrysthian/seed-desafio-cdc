package com.rfdev.desafio_cdc.compra.pagamento;

import com.rfdev.desafio_cdc.compra.Compra;
import com.rfdev.desafio_cdc.config.Documento;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Getter
public class RealizaPagamentoRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    @Documento
    private String documento;

    @NotBlank
    private String endereco;

    @NotBlank
    private String complemento;

    @NotBlank
    private String cidade;

    @NotNull
    @EntidadeExiste(message = "Pais não encontrado", nomeTabela = Pais.class, nomeCampo = "id")
    private UUID paisId;

    @EntidadeExiste(message = "Estado não encontrado", nomeTabela = Estado.class, nomeCampo = "id")
    private UUID estadoId;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    @NotNull
    @Valid
    private CarrinhoDeCompraRequest carrinho;


    public Compra toModel(EntityManager entityManager) {
        Pais pais = entityManager.find(Pais.class, this.paisId);
        Estado estado = this.estadoId != null ? entityManager.find(Estado.class, this.estadoId) : null;

        return new Compra(
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
    }

}
