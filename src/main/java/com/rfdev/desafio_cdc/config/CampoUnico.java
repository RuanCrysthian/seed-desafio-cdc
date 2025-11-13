package com.rfdev.desafio_cdc.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CampoUnicoValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface CampoUnico {

    String message() default "Valor já existe no sistema.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> nomeTabela();

    String nomeCampo();
}
