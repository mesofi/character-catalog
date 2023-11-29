/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
    private HttpInputMessage httpInputMessage;
    @Mock
    private WebRequest webRequest;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Method method;

    @BeforeEach
    public void beforeEach() {
        exceptionHandler = new ApplicationExceptionHandler();
    }

    /**
     * {@link ApplicationExceptionHandler#handleHttpMessageNotReadable(HttpMessageNotReadableException, org.springframework.http.HttpHeaders, HttpStatusCode, org.springframework.web.context.request.WebRequest)}
     */
    @Test
    public void should_verify_response_for_message_not_readable() {
        String errorMsg = "This is my error message";
        HttpStatusCode OK = HttpStatusCode.valueOf(200);
        HttpHeaders headers = new HttpHeaders();

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(errorMsg, httpInputMessage);
        ResponseEntity<Object> response = exceptionHandler.handleHttpMessageNotReadable(ex, headers, OK, webRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());

        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();

        assertNotNull(response);
        assertNotNull(apiErrorResponse);
        assertEquals("This is my error message", apiErrorResponse.getMessage());
    }

    /**
     * {@link ApplicationExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, HttpStatusCode, org.springframework.web.context.request.WebRequest)}
     */
    @Test
    public void should_verify_response_for_argument_not_valid() {
        HttpStatusCode OK = HttpStatusCode.valueOf(200);
        HttpHeaders headers = new HttpHeaders();

        List<FieldError> fields = new ArrayList<>();
        FieldError fieldError1 = new FieldError("objectName", "field", "default");
        fields.add(fieldError1);

        List<ObjectError> errors = new ArrayList<>();
        ObjectError objectError1 = new ObjectError("objectName", null);
        errors.add(objectError1);

        when(method.getParameterCount()).thenReturn(1);
        when(bindingResult.getFieldErrors()).thenReturn(fields);
        when(bindingResult.getGlobalErrors()).thenReturn(errors);

        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);
        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex, headers, OK, webRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());

        ApiErrorResponse apiErrorResponse = (ApiErrorResponse) response.getBody();

        assertNotNull(response);
        assertNotNull(apiErrorResponse);
        assertEquals("Validation failed for argument [0] in null: ", apiErrorResponse.getMessage());
        Set<String> expectedErrors = apiErrorResponse.getErrors();
        assertNotNull(expectedErrors);
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("field: default, objectName: null");
        assertEquals(treeSet.toString(), expectedErrors.toString());
    }
}
