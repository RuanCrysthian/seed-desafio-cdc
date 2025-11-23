package com.rfdev.desafio_cdc.config;

import com.rfdev.desafio_cdc.compra.pagamento.CarrinhoDeCompraRequest;
import com.rfdev.desafio_cdc.compra.pagamento.RealizaPagamentoRequest;
import com.rfdev.desafio_cdc.livro.Livro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class TotalCompraValidator implements Validator {

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

        if (request.carrinho().total() != null) {
            CarrinhoDeCompraRequest carrinho = request.carrinho();
            var soma = carrinho.itens().stream()
                .map(item -> {
                    Livro livro = entityManager.find(Livro.class, item.idLivro());
                    return livro.getPreco().multiply(new BigDecimal(item.quantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (soma.compareTo(carrinho.total()) != 0) {
                errors.rejectValue("carrinho.total", null, "O total informado não corresponde à soma dos itens no carrinho.");
            }
        }
    }
}
