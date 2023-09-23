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
    public void should_return_null_price_when_input_is_null_and_empty(String input) {
        assertNull(CommonUtils.toPrice(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "¥12,500", "¥0" })
    public void should_return_valid_price(String input) {
        assertNotNull(CommonUtils.toPrice(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void should_return_null_local_date_when_input_is_null_and_empty(String input) {
        assertNull(CommonUtils.toDate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = { "9/15/2023", "10/30/2020", "7/3/2020", "11/2022", "5/2021" })
    public void should_return_valid_local_date(String input) {
        assertNotNull(CommonUtils.toDate(input));
    }
}