package com.rfdev.desafio_cdc.config;

import com.rfdev.desafio_cdc.compra.pagamento.RealizaPagamentoRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class EstadoPertenceAoPaisValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return RealizaPagamentoRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, org.springframework.validation.Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RealizaPagamentoRequest request = (RealizaPagamentoRequest) target;

        if (request.estadoId() != null) {
            Long count = entityManager.createQuery(
                    "SELECT COUNT(e) FROM Estado e WHERE e.id = :estadoId AND e.pais.id = :paisId", Long.class)
                .setParameter("estadoId", request.estadoId())
                .setParameter("paisId", request.paisId())
                .getSingleResult();

            if (count == 0) {
                errors.rejectValue("estadoId", null, "O estado selecionado não pertence ao país informado.");
            }
        }
    }
}
