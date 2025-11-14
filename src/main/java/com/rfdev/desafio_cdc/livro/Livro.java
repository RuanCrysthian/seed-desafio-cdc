package com.rfdev.desafio_cdc.livro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.categoria.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livros")
@Getter
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "livro_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "titulo", nullable = false, unique = true)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    @Column(name = "resumo", nullable = false, length = 500)
    private String resumo;

    @Column(name = "sumario", columnDefinition = "TEXT")
    private String sumario;

    @Min(20)
    @NotNull
    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Min(100)
    @NotNull
    @Column(name = "numero_paginas", nullable = false)
    private Integer numeroPaginas;

    @NotBlank
    private String isbn;

    @NotNull
    @Future
    @Column(name = "data_publicacao", nullable = false)
    private LocalDateTime dataPublicacao;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @Deprecated
    public Livro() {
    }

    public Livro(
        @NotBlank String titulo,
        @NotBlank @Size(max = 500) String resumo,
        String sumario,
        @NotNull @Min(20) BigDecimal preco,
        @NotNull @Min(100) Integer numeroPaginas,
        @NotBlank String isbn,
        @NotNull @Future LocalDateTime dataPublicacao,
        @NotNull Categoria categoria,
        @NotNull Autor autor
    ) {
        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.categoria = categoria;
        this.autor = autor;
    }
}
