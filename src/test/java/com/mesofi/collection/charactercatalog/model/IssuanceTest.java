/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test for {@link Issuance}
 *
 * @author armandorivasarzaluz
 */
public class IssuanceTest {

  private Issuance issuance;

  @BeforeEach
  public void beforeEach() {
    issuance = new Issuance();
  }

  @ParameterizedTest
  @MethodSource("provideBooleanValues")
  public void should_verify_getter_setter_properties(
      Boolean preorderConfirmationDay, Boolean releaseConfirmationDay) {
    issuance.setBasePrice(new BigDecimal("4.5"));
    issuance.setReleasePrice(new BigDecimal("6.5"));
    issuance.setFirstAnnouncementDate(LocalDate.of(2023, 3, 5));
    issuance.setPreorderDate(LocalDate.of(2023, 5, 3));
    issuance.setPreorderConfirmationDay(preorderConfirmationDay);
    issuance.setReleaseDate(LocalDate.of(2023, 1, 1));
    issuance.setReleaseConfirmationDay(releaseConfirmationDay);

    assertEquals(new BigDecimal("4.5"), issuance.getBasePrice());
    assertEquals(new BigDecimal("6.5"), issuance.getReleasePrice());
    assertEquals(LocalDate.of(2023, 3, 5), issuance.getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2023, 5, 3), issuance.getPreorderDate());
    assertEquals(preorderConfirmationDay, issuance.getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2023, 1, 1), issuance.getReleaseDate());
    assertEquals(releaseConfirmationDay, issuance.getReleaseConfirmationDay());
  }

  private static Stream<Arguments> provideBooleanValues() {
    // @formatter:off
    return Stream.of(
        Arguments.of(null, null), Arguments.of(true, true), Arguments.of(false, false));
    // @formatter:on
  }
}
