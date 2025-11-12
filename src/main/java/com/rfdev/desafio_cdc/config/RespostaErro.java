package com.rfdev.desafio_cdc.config;

import java.time.LocalDateTime;
import java.util.List;

public record RespostaErro(
    LocalDateTime timestamp,
    int status,
    String erro,
    List<String> mensagens
) {
}
