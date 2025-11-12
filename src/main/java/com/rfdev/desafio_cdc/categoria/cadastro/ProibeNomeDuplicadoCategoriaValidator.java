package com.rfdev.desafio_cdc.categoria.cadastro;

import com.rfdev.desafio_cdc.categoria.CategoriaRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProibeNomeDuplicadoCategoriaValidator implements Validator {

    private final CategoriaRepository categoriaRepository;

    public ProibeNomeDuplicadoCategoriaValidator(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CadastroCategoriaRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }
        CadastroCategoriaRequest request = (CadastroCategoriaRequest) target;
        if (categoriaRepository.existsByNome(request.nome())) {
            errors.rejectValue("nome", null, "Já existe uma categoria cadastrada com esse nome");
        }
    }
}
