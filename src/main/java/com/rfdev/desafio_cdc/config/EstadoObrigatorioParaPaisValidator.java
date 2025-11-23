package com.rfdev.desafio_cdc.config;

import com.rfdev.desafio_cdc.compra.pagamento.RealizaPagamentoRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EstadoObrigatorioParaPaisValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return RealizaPagamentoRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RealizaPagamentoRequest request = (RealizaPagamentoRequest) target;

        Long countEstados = entityManager.createQuery(
                "SELECT COUNT(e) FROM Estado e WHERE e.pais.id = :paisId", Long.class)
            .setParameter("paisId", request.paisId())
            .getSingleResult();

        boolean paisTemEstados = countEstados > 0;
        if (paisTemEstados && request.estadoId() == null) {
            errors.rejectValue("estadoId", null, "Estado é obrigatório para o país selecionado.");
        }

    }
}
