/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link CharacterFigureNotFoundException}
 * 
 * @author armandorivasarzaluz
 */
public class CharacterFigureNotFoundExceptionTest {

    //@Test
    public void should_verify_error_message() {
        CharacterFigureNotFoundException notFoundException = new CharacterFigureNotFoundException("My Error message");

        assertEquals("My Error message", notFoundException.getMessage());
    }
}
