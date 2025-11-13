package com.rfdev.desafio_cdc.livro.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.autor.AutorRepository;
import com.rfdev.desafio_cdc.categoria.Categoria;
import com.rfdev.desafio_cdc.categoria.CategoriaRepository;
import com.rfdev.desafio_cdc.config.CampoUnico;
import com.rfdev.desafio_cdc.config.EntidadeExiste;
import com.rfdev.desafio_cdc.livro.Livro;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CadastroLivroRequest(
    @NotBlank @CampoUnico(message = "Já existe um livro com esse título", nomeTabela = Livro.class, nomeCampo = "titulo") String titulo,
    @NotBlank @Size(max = 200) String resumo,
    String sumario,
    @NotNull @Min(20) BigDecimal preco,
    @NotNull @Min(100) Integer numeroPaginas,
    @NotBlank @CampoUnico(message = "Já existe um livro com esse ISBN", nomeTabela = Livro.class, nomeCampo = "isbn") String isbn,
    @NotNull @Future LocalDateTime dataPublicacao,
    @NotNull @EntidadeExiste(message = "Categoria não encontrada", nomeTabela = Categoria.class, nomeCampo = "id") UUID categoriaId,
    @NotNull @EntidadeExiste(message = "Autor não encontrado", nomeTabela = Autor.class, nomeCampo = "id") UUID autorId
) {

    public Livro toModel(CategoriaRepository categoriaRepository, AutorRepository autorRepository) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        Autor autor = autorRepository.findById(autorId)
            .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
        return new Livro(titulo, resumo, sumario, preco, numeroPaginas, isbn, dataPublicacao, categoria, autor);
    }
}
