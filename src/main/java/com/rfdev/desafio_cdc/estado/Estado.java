package com.rfdev.desafio_cdc.estado;

import com.rfdev.desafio_cdc.pais.Pais;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "estados")
@Getter
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "estado_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;

    @Deprecated
    public Estado() {
    }

    public Estado(@NotBlank String nome, @NotNull Pais pais) {
        this.nome = nome;
        this.pais = pais;
    }
}
