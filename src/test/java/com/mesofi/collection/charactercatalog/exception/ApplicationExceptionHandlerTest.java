/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

/**
 * Test for {@link ApplicationExceptionHandler}
 * 
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private CharacterFigureNotFoundException characterFigureNotFoundException;
    @Mock
    private IllegalArgumentException illegalArgumentException;
    @Mock
    private RuntimeException runtimeException;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void beforeEach() {
        exceptionHandler = new ApplicationExceptionHandler();
    }

    /**
     * {@link ApplicationExceptionHandler#handleHttpMessageNotReadable(HttpMessageNotReadableException, org.springframework.http.HttpHeaders, HttpStatusCode, org.springframework.web.context.request.WebRequest)}
     */
    @Test
    public void should_verify_response_for_message_not_readable() {
        when(httpMessageNotReadableException.getMessage()).thenReturn("The error");

        ResponseEntity<Object> response = exceptionHandler.handleHttpMessageNotReadable(httpMessageNotReadableException, null, HttpStatusCode.valueOf(404), webRequest);
        assertNotNull(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("The error", apiErrorResponse.getMessage());
    }

    /**
     * {@link ApplicationExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, HttpStatusCode, org.springframework.web.context.request.WebRequest)}
     */
    @Test
    public void should_verify_response_for_argument_not_valid() {
        when(methodArgumentNotValidException.getMessage()).thenReturn("The error");
        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(new FieldError("ddd", "myField", "Invalid value"));
        when(bindingResult.getFieldErrors()).thenReturn(errors);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException, null, HttpStatusCode.valueOf(404), webRequest);
        assertNotNull(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("The error", apiErrorResponse.getMessage());
        assertEquals(Set.of("myField: Invalid value"), apiErrorResponse.getErrors());
    }

    /**
     * {@link ApplicationExceptionHandler#handleNotFound(CharacterFigureNotFoundException, WebRequest)}
     */
    @Test
    public void should_verify_response_for_handle_not_found() {
        when(characterFigureNotFoundException.getMessage()).thenReturn("The error");

        ResponseEntity<Object> response = exceptionHandler.handleNotFound(characterFigureNotFoundException, webRequest);
        assertNotNull(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("The error", apiErrorResponse.getMessage());
    }

    /**
     * {@link ApplicationExceptionHandler#handleGenericError(RuntimeException, WebRequest)}
     */
    @Test
    public void should_verify_response_for_handle_bad_request() {
        when(illegalArgumentException.getMessage()).thenReturn("The error");

        ResponseEntity<Object> response = exceptionHandler.handleBadRequest(illegalArgumentException, webRequest);
        assertNotNull(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("The error", apiErrorResponse.getMessage());
    }

    /**
     * {@link ApplicationExceptionHandler#handleGenericError(RuntimeException, WebRequest)}
     */
    @Test
    public void should_verify_response_for_handle_generic_error() {
        when(runtimeException.getMessage()).thenReturn("The error");

        ResponseEntity<Object> response = exceptionHandler.handleGenericError(runtimeException, webRequest);
        assertNotNull(response);
        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();
        assertNotNull(apiErrorResponse);
        assertEquals("The error", apiErrorResponse.getMessage());
    }
}
