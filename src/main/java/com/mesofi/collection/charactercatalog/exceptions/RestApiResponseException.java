package com.mesofi.collection.charactercatalog.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestApiResponseException extends ApiResponseException {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {

        return handleApiException(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(value = { NoSuchCharacterFoundException.class })
    protected ResponseEntity<Object> handleNotFoundRequest(RuntimeException ex, WebRequest request) {

        return handleApiException(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, null);
    }
}
