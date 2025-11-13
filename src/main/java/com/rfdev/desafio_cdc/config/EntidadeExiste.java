package com.rfdev.desafio_cdc.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EntidadeExisteValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntidadeExiste {

    String message() default "Entidade não encontrada no sistema.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> nomeTabela();

    String nomeCampo();

}
