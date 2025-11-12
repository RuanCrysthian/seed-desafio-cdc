package com.rfdev.desafio_cdc.categoria;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "categorias")
@Getter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "categoria_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Deprecated
    public Categoria() {
    }

    public Categoria(@NotBlank String nome) {
        this.nome = nome;
    }
}
