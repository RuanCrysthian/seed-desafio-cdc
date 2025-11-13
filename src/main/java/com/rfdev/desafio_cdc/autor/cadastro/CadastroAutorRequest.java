package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.Autor;
import com.rfdev.desafio_cdc.config.CampoUnico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroAutorRequest(
    @NotBlank String nome,
    @NotBlank @Email @CampoUnico(message = "Email já cadastrado", nomeTabela = Autor.class, nomeCampo = "email") String email,
    @NotBlank @Size(max = 400) String descricao
) {

    public Autor toModel() {
        return new Autor(
            this.nome(),
            this.email(),
            this.descricao()
        );
    }
}
