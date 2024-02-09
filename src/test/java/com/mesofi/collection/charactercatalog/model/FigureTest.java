/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for {@link Figure}
 *
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class FigureTest {

  private CharacterFigure characterFigure;

  @Mock private Figure figure;

  @BeforeEach
  public void beforeEach() {
    characterFigure = new CharacterFigure();
  }

  @ParameterizedTest
  @MethodSource("provideBooleanValues")
  public void should_verify_getter_setter_properties(boolean futureRelease) {
    Issuance issuanceJPY = new Issuance();
    Issuance issuanceMXN = new Issuance();

    characterFigure.setIssuanceJPY(issuanceJPY);
    characterFigure.setIssuanceMXN(issuanceMXN);
    characterFigure.setFutureRelease(futureRelease);
    characterFigure.setUrl("https://imagizer.imageshack.com/v2/1024x768q70/923/8Qfvaa.jpg");
    characterFigure.setDistribution(Distribution.TAMASHII_STORE);
    characterFigure.setRemarks("No comments");

    assertEquals(issuanceJPY, characterFigure.getIssuanceJPY());
    assertEquals(issuanceMXN, characterFigure.getIssuanceMXN());
    assertEquals(futureRelease, characterFigure.isFutureRelease());
    assertEquals(
        "https://imagizer.imageshack.com/v2/1024x768q70/923/8Qfvaa.jpg", characterFigure.getUrl());
    assertEquals(Distribution.TAMASHII_STORE, characterFigure.getDistribution());
    assertEquals("No comments", characterFigure.getRemarks());
  }

  @Test
  public void should_verify_equality() {
    Figure mockFigure1 = new Figure() {};
    Figure mockFigure2 = new Figure() {};

    assertEquals(mockFigure2, mockFigure2);
    assertNotEquals("", mockFigure1);

    when(figure.canEqual(mockFigure1)).thenReturn(true);
    assertEquals(mockFigure1, figure);

    when(figure.canEqual(mockFigure1)).thenReturn(false);
    assertNotEquals(mockFigure1, figure);
  }

  @Test
  public void should_verify_same_type() {
    CharacterFigure mockFigure2 = new CharacterFigure();
    assertTrue(characterFigure.canEqual(mockFigure2));
    assertFalse(characterFigure.canEqual("Other"));
  }

  @Test
  public void should_get_hash_code() {
    Figure theCharacterFigure = new Figure() {};
    assertEquals(1, theCharacterFigure.hashCode());
  }

  private static Stream<Arguments> provideBooleanValues() {
    // @formatter:off
    return Stream.of(Arguments.of(true, true), Arguments.of(false, false));
    // @formatter:on
  }
}
