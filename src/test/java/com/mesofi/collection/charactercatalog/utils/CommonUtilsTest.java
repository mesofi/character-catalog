/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 30, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for {@link CommonUtils}
 * 
 * @author armandorivasarzaluz
 */
public class CommonUtilsTest {

    //@Test
    public void should_validate_private_access() throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<CommonUtils> constructor = CommonUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    //@Test
    public void should_return_null_price() {
        assertNull(CommonUtils.toPrice((String) null));
    }

    //@ParameterizedTest
    @ValueSource(strings = { "¥12,500", "¥0", "$11.9", "3.4" })
    public void should_return_valid_price(final String input) {
        assertNotNull(CommonUtils.toPrice(input));
    }

    //@Test
    public void should_return_convert_price_to_string() {
        BigDecimal decimal = null;
        assertNull(CommonUtils.toPrice(decimal));

        decimal = new BigDecimal("34");
        assertEquals("34", CommonUtils.toPrice(decimal));
    }

    //@ParameterizedTest
    @NullAndEmptySource
    public void should_return_null_local_date_when_input_is_null_and_empty(String input) {
        assertNull(CommonUtils.toDate(input));
    }

    //@ParameterizedTest
    @ValueSource(strings = { "9/15/2023", "10/30/2020", "7/3/2020", "11/2022", "5/2021" })
    public void should_return_valid_local_date(String input) {
        assertNotNull(CommonUtils.toDate(input));
    }

   // @Test
    public void should_return_convert_date_to_string() {
        LocalDate localDate = null;
        assertNull(CommonUtils.toDate(localDate));

        localDate = LocalDate.of(2022, 2, 3);
        assertEquals("2/3/2022", CommonUtils.toDate(localDate));
    }

    //@Test
    public void should_return_convert_date_with_month_year_only_to_string() {
        LocalDate localDate = LocalDate.of(2022, 2, 3);
        assertEquals("2/2022", CommonUtils.toDate(localDate, true));

        localDate = LocalDate.of(2023, 10, 10);
        assertEquals("10/10/2023", CommonUtils.toDate(localDate, false));
    }

    //@Test
    public void should_return_valid_boolean_value() {
        assertFalse(CommonUtils.toBoolean(null));
        assertFalse(CommonUtils.toBoolean(""));
        assertFalse(CommonUtils.toBoolean("dsf"));
        assertFalse(CommonUtils.toBoolean("true"));
        assertTrue(CommonUtils.toBoolean("TRUE"));
    }

    //@Test
    public void should_return_valid_integer_value() {
        assertNull(CommonUtils.toInteger(null));
        assertNull(CommonUtils.toInteger(""));
        assertEquals(5, CommonUtils.toInteger("5"));
    }

    //@Test
    public void should_return_valid_string_value() {
        assertNull(CommonUtils.toStringValue(null));
        assertNull(CommonUtils.toStringValue(""));
        assertEquals("5", CommonUtils.toStringValue("5"));
    }

    //@Test
    public void should_return_valid_set_value() {
        assertNull(CommonUtils.toSetValue(null));
        assertNull(CommonUtils.toSetValue(""));

        Set<String> list = CommonUtils.toSetValue("3");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertTrue(list.contains("3"));

        list = CommonUtils.toSetValue("ikki, ex, gold,seiya");
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.contains("ikki"));
        assertTrue(list.contains("ex"));
        assertTrue(list.contains("gold"));
        assertTrue(list.contains("seiya"));
    }

    //@Test
    public void should_return_valid_list_value() {
        assertNull(CommonUtils.toListValue(null));
        assertEquals(List.of("test", "test2", "test3"), CommonUtils.toListValue("test,test2,test3"));
    }

    //@Test
    public void should_validate_day_month_date() {
        assertNull(CommonUtils.isDayMonthYear(null));

        assertFalse(CommonUtils.isDayMonthYear("03/4"));

        assertTrue(CommonUtils.isDayMonthYear("03/4/2023"));
    }

    //@Test
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
