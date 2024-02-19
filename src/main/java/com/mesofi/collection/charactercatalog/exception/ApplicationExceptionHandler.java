/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Error handling.
 *
 * @author armandorivasarzaluz
 */
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.debug("Handle missing servlet request part exception ...");

    return createCustomRequestEntity(ex, headers, status, request);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    log.debug("Handle HTTP message not readable exception ...");

    return createCustomRequestEntity(ex, headers, status, request);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    log.debug("Handle HTTP media type not supported exception ...");

    return createCustomRequestEntity(ex, headers, status, request);
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    log.debug("Handle method argument not valid exception ...");

    ResponseEntity<Object> response = createCustomRequestEntity(ex, headers, status, request);
    ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
    apiErrorResponse.setErrors(getErrors(ex));
    return response;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, final WebRequest request) {
    log.debug("Handle type mismatch exception ...");

    ApiErrorResponse body = new ApiErrorResponse();
    Optional.ofNullable(ex.getRequiredType())
        .ifPresentOrElse(
            requiredType -> {
              String name = ex.getName();
              String type = requiredType.getSimpleName();
              Object value = ex.getValue();
              String message =
                  String.format("'%s' should be a valid '%s' and '%s' isn't", name, type, value);
              body.setMessage(message);
            },
            () -> body.setMessage(ex.getMessage()));

    return createResponseEntity(body, new HttpHeaders(), BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {CharacterFigureNotFoundException.class})
  public ResponseEntity<Object> handleNotFound(
      CharacterFigureNotFoundException ex, final WebRequest request) {
    log.debug("Handle character not found exception ...");

    return createCustomRequestEntity(ex, new HttpHeaders(), NOT_FOUND, request);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<Object> handleBadRequest(
      IllegalArgumentException ex, final WebRequest request) {
    log.debug("Handle invalid request exception ...");

    return createCustomRequestEntity(ex, new HttpHeaders(), BAD_REQUEST, request);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity<Object> handleGenericError(RuntimeException ex, final WebRequest request) {
    log.debug("Unhandled exception ...");

    return createCustomRequestEntity(ex, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
  }

  /**
   * Creates a custom request entity.
   *
   * @param ex The source exception.
   * @param headers The headers.
   * @param status The status.
   * @param request The request.
   * @return The custom response.
   */
  private ResponseEntity<Object> createCustomRequestEntity(
      Exception ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ApiErrorResponse body = new ApiErrorResponse();
    body.setMessage(Optional.ofNullable(ex.getMessage()).orElseGet(() -> "Error not defined"));
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
}
