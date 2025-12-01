package com.rfdev.desafio_cdc.pais;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rfdev.desafio_cdc.estado.Estado;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Table(name = "paises")
@Getter
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pais_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estado> estados = new ArrayList<>();

    @Deprecated
    public Pais() {
    }

    public Pais(@NotBlank String nome) {
        this.nome = nome;
    }

    public void adicionarEstado(Estado estado) {
        if (estado.getPais() != this) {
            throw new IllegalArgumentException("Estado pertence a outro país");
        }
        estados.add(estado);
    }

    public Boolean possuiEstados() {
        return !estados.isEmpty();
    }

}
