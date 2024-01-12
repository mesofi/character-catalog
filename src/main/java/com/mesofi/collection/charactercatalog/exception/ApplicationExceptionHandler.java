/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Error handling.
 * 
 * @author armandorivasarzaluz
 */
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        log.debug("Handle http message not readable exception ...");

        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        return createResponseEntity(body, headers, status, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        log.debug("Handle method argument not valid exception ...");

        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        body.setErrors(getErrors(ex));
        return createResponseEntity(body, headers, status, request);
    }

    @ExceptionHandler(value = { CharacterFigureNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(CharacterFigureNotFoundException ex, final WebRequest request) {
        log.debug("Handle character not found exception ...");

        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        return createResponseEntity(body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex, final WebRequest request) {
        log.debug("Handle invalid request exception ...");

        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        return createResponseEntity(body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleGenericError(RuntimeException ex, final WebRequest request) {
        log.debug("Unhandle exception ...");

        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        return createResponseEntity(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private Set<String> getErrors(MethodArgumentNotValidException ex) {
        Set<String> errors = new TreeSet<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return errors;
    }
}
