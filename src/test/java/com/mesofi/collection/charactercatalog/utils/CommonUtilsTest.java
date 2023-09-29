/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
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

    @Test
    public void should_return_valid_boolean_value() {
        assertFalse(CommonUtils.toBoolean(null));
        assertFalse(CommonUtils.toBoolean(""));
        assertFalse(CommonUtils.toBoolean("dsf"));
        assertFalse(CommonUtils.toBoolean("true"));
        assertTrue(CommonUtils.toBoolean("TRUE"));
    }

    @Test
    public void should_return_valid_integer_value() {
        assertNull(CommonUtils.toInteger(null));
        assertNull(CommonUtils.toInteger(""));
        assertEquals(5, CommonUtils.toInteger("5"));
    }

    @Test
    public void should_return_valid_string_value() {
        assertNull(CommonUtils.toStringValue(null));
        assertNull(CommonUtils.toStringValue(""));
        assertEquals("5", CommonUtils.toStringValue("5"));
    }

    @Test
    public void should_validate_day_month_date() {
        assertNull(CommonUtils.isDayMonthYear(null));

        assertFalse(CommonUtils.isDayMonthYear("03/4"));

        assertTrue(CommonUtils.isDayMonthYear("03/4/2023"));
    }

    @Test
    public void should_reverse_list() {
        List<String> myList = null;
        CommonUtils.reverseListElements(myList);
        assertNull(myList);

        myList = new ArrayList<>();

        CommonUtils.reverseListElements(myList);
        assertNotNull(myList);

        myList.add("1");
        CommonUtils.reverseListElements(myList);
        assertEquals("1", myList.get(0));

        myList.add("a");
        myList.add("b");
        myList.add("c");
        myList.add("d");
        CommonUtils.reverseListElements(myList);
        assertEquals("d", myList.get(0));
        assertEquals("c", myList.get(1));
        assertEquals("b", myList.get(2));
        assertEquals("a", myList.get(3));
        assertEquals("1", myList.get(4));
    }
}
