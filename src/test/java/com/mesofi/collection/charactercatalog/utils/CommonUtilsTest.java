/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for {@link CommonUtils}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
public class CommonUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void should_return_null_when_input_is_null_and_empty(String input) {
        assertNull(CommonUtils.toPrice(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "¥12,500", "¥0" })
    public void should_return_valid_price(String input) {
        assertNotNull(CommonUtils.toPrice(input));
    }
}
