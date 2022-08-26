package com.mesofi.collection.charactercatalog.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class ApiResponseException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleApiException(ex, headers, status, null);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleApiException(ex, headers, status, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return handleApiException(ex, headers, status, errors);
    }

    protected ResponseEntity<Object> handleApiException(Exception ex, HttpHeaders headers, HttpStatus status,
            List<String> errors) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }

        ApiException apiException = new ApiException(status, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiException, headers, status);
    }

}
