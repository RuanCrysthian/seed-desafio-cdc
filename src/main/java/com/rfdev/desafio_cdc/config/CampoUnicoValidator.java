package com.rfdev.desafio_cdc.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CampoUnicoValidator implements ConstraintValidator<CampoUnico, Object> {

    private Class<?> nomeTabela;
    private String nomeCampo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(CampoUnico constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.nomeTabela = constraintAnnotation.nomeTabela();
        this.nomeCampo = constraintAnnotation.nomeCampo();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String jpql = String.format(
            "SELECT COUNT(e) FROM %s e WHERE e.%s = :value",
            nomeTabela.getSimpleName(), nomeCampo
        );

        Query query = entityManager.createQuery("SELECT COUNT(e) FROM " + nomeTabela.getSimpleName() + " e WHERE e." + nomeCampo + " = :value", Long.class);
        query.setParameter("value", value);
        Long count = (Long) query.getSingleResult();

        return count == 0;
    }
}