package com.rfdev.desafio_cdc.config;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidadorErrosHandler {

    private MessageSource messageSource;

    public ValidadorErrosHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ResponseEntity<RespostaErro> buildRespostaErro(@NotNull HttpStatus status, @NotNull String error, @NotNull List<String> messages) {
        return ResponseEntity.status(status).body(new RespostaErro(LocalDateTime.now(), status.value(), error, messages));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
            .collect(Collectors.toList());

        return buildRespostaErro(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos", messages);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RespostaErro> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> messages = ex.getConstraintViolations()
            .stream()
            .map(violation -> violation.getMessage())
            .collect(Collectors.toList());

        return buildRespostaErro(HttpStatus.BAD_REQUEST, "Violação de restrições", messages);
    }


}
