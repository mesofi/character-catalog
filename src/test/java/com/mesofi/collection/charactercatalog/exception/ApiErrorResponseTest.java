/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 28, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link ApiErrorResponse}
 *
 * @author armandorivasarzaluz
 */
public class ApiErrorResponseTest {

  private ApiErrorResponse apiErrorResponse;

  @BeforeEach
  public void beforeEach() {
    apiErrorResponse = new ApiErrorResponse();
  }

  @Test
  public void should_verify_getter_setter_properties() {
    apiErrorResponse.setErrors(Set.of("Error1"));
    apiErrorResponse.setMessage("My Error message");
    apiErrorResponse.setTimestamp(LocalDateTime.of(2023, 10, 2, 4, 5));

    assertEquals(Set.of("Error1"), apiErrorResponse.getErrors());
    assertEquals("My Error message", apiErrorResponse.getMessage());
    assertEquals(LocalDateTime.of(2023, 10, 2, 4, 5), apiErrorResponse.getTimestamp());
  }
}
