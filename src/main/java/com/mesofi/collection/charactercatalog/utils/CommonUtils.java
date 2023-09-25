/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import org.springframework.util.StringUtils;

/**
 * Common utilities.
 * 
 * @author armandorivasarzaluz
 *
 */
public class CommonUtils {

    private CommonUtils() {

    }

    /**
     * Converts a string into a valid BigDecimal reference.
     * 
     * @param value The value to be converted.
     * @return A BigDecimal reference or null if the input is null.
     */
    public static BigDecimal toPrice(final String value) {
        if (StringUtils.hasText(value)) {
            return new BigDecimal(value.substring(1).replace(',', '.'));
        }
        return null;
    }

    /**
     * Converts a string into a valid LocalDate reference.
     * 
     * @param value The value to be converted.
     * @return A LocalDate reference or null if the input is null.
     */
    public static LocalDate toDate(final String value) {
        if (StringUtils.hasText(value)) {
            boolean yearMonthDay = Boolean.TRUE.equals(isDayMonthYear(value));
            // @formatter:off
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("M")
                    .appendLiteral("/")
                    .optionalStart()
                    .appendPattern("d")
                    .appendLiteral("/")
                    .optionalEnd()
                    .appendValue(ChronoField.YEAR, 4)
                    .toFormatter();
            // @formatter:on

            return yearMonthDay ? LocalDate.parse(value, formatter) : YearMonth.parse(value, formatter).atDay(1);
        }
        return null;
    }

    /**
     * Gets the boolean value based on a string value.
     * 
     * @param value TRUE, true, false otherwise.
     * @return true or false.
     */
    public static boolean toBoolean(final String value) {
        if (StringUtils.hasText(value)) {
            return "TRUE".equals(value);
        }
        return false;
    }

    /**
     * Gets the integer value based on a string value.
     * 
     * @param value An integer value.
     * @return The integer.
     */
    public static Integer toInteger(final String value) {
        if (StringUtils.hasText(value)) {
            return Integer.parseInt(value);
        }
        return null;
    }

    /**
     * Gets the String representation based on another String value.
     * 
     * @param value String value to be converted into another String value.
     * @return A null value if the input String is empty.
     */
    public static String toStringValue(final String value) {
        if (StringUtils.hasText(value)) {
            return value.trim();
        }
        return null;
    }

    /**
     * Test if the value passed in represents a date compound of year, month and
     * day.
     * 
     * @param value The value to be tested.
     * @return true, if it's a valid date.
     */
    public static Boolean isDayMonthYear(final String value) {
        int count = 0;
        if (StringUtils.hasText(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '/') {
                    count++;
                }
            }
        }
        if (count == 0) {
            return null;
        }
        return count != 1;
    }
}
