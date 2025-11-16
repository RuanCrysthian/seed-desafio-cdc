package com.rfdev.desafio_cdc.compra;

import com.rfdev.desafio_cdc.estado.Estado;
import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "compras")
@Getter
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    private String documento;

    @NotBlank
    private String endereco;

    @NotBlank
    private String complemento;

    @NotBlank
    private String cidade;

    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    @NotNull
    private Pais pais;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Pedido pedido;

    @Deprecated
    public Compra() {
    }

    public Compra(
        @NotBlank @Email String email,
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank String documento,
        @NotBlank String endereco,
        @NotBlank String complemento,
        @NotBlank String cidade,
        @NotNull Pais pais, Estado estado,
        @NotBlank String telefone,
        @NotBlank String cep,
        @NotNull Pedido pedido) {
        this.email = email;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.documento = documento;
        this.endereco = endereco;
        this.complemento = complemento;
        this.cidade = cidade;
        this.pais = pais;
        this.estado = estado;
        this.telefone = telefone;
        this.cep = cep;
        this.pedido = pedido;
    }
}
