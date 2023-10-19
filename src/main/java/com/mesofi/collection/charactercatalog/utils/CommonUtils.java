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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (value.contains("$") || value.contains("¥")) {
                return new BigDecimal(value.substring(1).replace(",", ""));
            } else {
                return new BigDecimal(value.replace(",", ""));
            }
        }
        return null;
    }

    /**
     * Converts a BigDecimal into a valid String reference.
     * 
     * @param value The value to be converted.
     * @return A BigDecimal string or null if the input is null.
     */
    public static String toPrice(final BigDecimal value) {
        return Objects.nonNull(value) ? value.toString() : null;
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
            DateTimeFormatter formatter = getDateTimeFormatter();
            return yearMonthDay ? LocalDate.parse(value, formatter) : YearMonth.parse(value, formatter).atDay(1);
        }
        return null;
    }

    /**
     * Converts a LocalDate reference into a valid String.
     * 
     * @param value The value to be converted.
     * @return A String or null if the input is null.
     */
    public static String toDate(final LocalDate value) {
        return Objects.nonNull(value) ? value.format(getDateTimeFormatter()) : null;
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        // @formatter:off
        return new DateTimeFormatterBuilder()
                .appendPattern("M")
                .appendLiteral("/")
                .optionalStart()
                .appendPattern("d")
                .appendLiteral("/")
                .optionalEnd()
                .appendValue(ChronoField.YEAR, 4)
                .toFormatter();
        // @formatter:on
    }

    /**
     * Gets the boolean value based on a string value.
     * 
     * @param value TRUE, true, false otherwise.
     * @return true or false.
     */
    public static boolean toBoolean(final String value) {
        return StringUtils.hasText(value) && "TRUE".equals(value);
    }

    /**
     * Gets the integer value based on a string value.
     * 
     * @param value An integer value.
     * @return The integer.
     */
    public static Integer toInteger(final String value) {
        return StringUtils.hasText(value) ? Integer.parseInt(value) : null;
    }

    /**
     * Gets the String representation based on another String value.
     * 
     * @param value String value to be converted into another String value.
     * @return A null value if the input String is empty.
     */
    public static String toStringValue(final String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    /**
     * Gets a list of elements based on a comma separated string.
     * 
     * @param value String value to be parsed to list.
     * @return A null value if the input String is empty.
     */
    public static Set<String> toSetValue(final String value) {
        return StringUtils.hasText(value)
                ? new HashSet<>(Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList()))
                : null;
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

    /**
     * Reverses the elements of an existing non-empty list.
     * 
     * @param <T>  The type of the list.
     * @param list The list to be reversed.
     */
    public static <T> void reverseListElements(final List<T> list) {
        if (Objects.nonNull(list) && !list.isEmpty()) {
            T value = list.remove(0);

            // call the recursive function to reverse
            // the list after removing the first element
            reverseListElements(list);

            // now after the rest of the list has been
            // reversed by the upper recursive call,
            // add the first value at the end
            list.add(value);
        }
    }
}
