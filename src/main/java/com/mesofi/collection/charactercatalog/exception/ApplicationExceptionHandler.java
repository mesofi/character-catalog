package com.mesofi.collection.charactercatalog.exception;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        return createResponseEntity(body, headers, status, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiErrorResponse body = new ApiErrorResponse();
        body.setMessage(ex.getMessage());
        body.setErrors(getErrors(ex));
        return createResponseEntity(body, headers, status, request);
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

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<?> handleNotFound(RuntimeException ex, final HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse();

        HttpStatusCode d = HttpStatusCode.valueOf(200);
        return new ResponseEntity<>(response, d);
    }

}
