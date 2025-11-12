package com.rfdev.desafio_cdc.autor.cadastro;

import com.rfdev.desafio_cdc.autor.AutorRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProibeEmailDuplicadoAutorValidor implements Validator {

    private final AutorRepository autorRepository;

    public ProibeEmailDuplicadoAutorValidor(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CadastroAutorRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        CadastroAutorRequest request = (CadastroAutorRequest) target;

        if (autorRepository.existsByEmail(request.email())) {
            errors.rejectValue("email", null, "Já existe um autor cadastrado com esse email");
        }

    }
}
