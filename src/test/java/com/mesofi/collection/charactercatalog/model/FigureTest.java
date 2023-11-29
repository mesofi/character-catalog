/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.*;
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

import com.mesofi.collection.charactercatalog.mock.MockFigure;

/**
 * Test for {@link Figure}
 * 
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class FigureTest {

    private MockFigure mockFigure;

    @Mock
    private Figure figure;

    @BeforeEach
    public void beforeEach() {
        mockFigure = new MockFigure();
    }

    @ParameterizedTest
    @MethodSource("provideBooleanValues")
    public void should_verify_getter_setter_properties(boolean futureRelease) {
        Issuance issuanceJPY = new Issuance();
        Issuance issuanceMXN = new Issuance();

        mockFigure.setIssuanceJPY(issuanceJPY);
        mockFigure.setIssuanceMXN(issuanceMXN);
        mockFigure.setFutureRelease(futureRelease);
        mockFigure.setUrl("https://imagizer.imageshack.com/v2/1024x768q70/923/8Qfvaa.jpg");
        mockFigure.setDistribution(Distribution.TAMASHII_STORE);
        mockFigure.setRemarks("No comments");

        assertEquals(issuanceJPY, mockFigure.getIssuanceJPY());
        assertEquals(issuanceMXN, mockFigure.getIssuanceMXN());
        assertEquals(futureRelease, mockFigure.isFutureRelease());
        assertEquals("https://imagizer.imageshack.com/v2/1024x768q70/923/8Qfvaa.jpg", mockFigure.getUrl());
        assertEquals(Distribution.TAMASHII_STORE, mockFigure.getDistribution());
        assertEquals("No comments", mockFigure.getRemarks());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void should_verify_equality() {
        MockFigure mockFigure1 = new MockFigure();
        MockFigure mockFigure2 = new MockFigure();
        MockFigure mockFigure3 = mockFigure2;

        assertEquals(mockFigure2, mockFigure3);
        assertNotEquals("", mockFigure1);

        when(figure.canEqual(mockFigure1)).thenReturn(true);
        assertEquals(mockFigure1, figure);

        when(figure.canEqual(mockFigure1)).thenReturn(false);
        assertNotEquals(mockFigure1, figure);
    }

    @Test
    public void should_verify_same_type() {
        MockFigure mockFigure2 = new MockFigure();
        assertTrue(mockFigure.canEqual(mockFigure2));
        assertFalse(mockFigure.canEqual("Other"));
    }

    @Test
    public void should_get_hash_code() {
        assertEquals(1, mockFigure.hashCode());
    }

    private static Stream<Arguments> provideBooleanValues() {
        // @formatter:off
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(false, false)
        );
        // @formatter:on
    }
}
