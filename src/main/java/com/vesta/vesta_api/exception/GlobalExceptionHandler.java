package com.vesta.vesta_api.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail httpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        if (cause instanceof tools.jackson.core.exc.StreamReadException) {
            ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "O formato do JSON enviado está incorreto ou possui erros de sintaxe");
            pd.setType(URI.create("about:blank"));
            pd.setProperty("code", "MALFORMED_JSON");
            pd.setProperty("errorMessage", e.getMessage());
            return pd;
        }

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "O corpo da requisição não pode ser vazio.");
        pd.setType(URI.create("about:blank"));
        pd.setProperty("code", "REQUIRED_BODY_MISSING");
        pd.setProperty("errorMessage", e.getMessage());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos. Verifique os dados e tente novamente.");
        pd.setType(URI.create("about:blank"));
        pd.setProperty("code", "INVALID_PARAMS");

        pd.setProperty("errorMessage", errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        return pd;
    }
 }
