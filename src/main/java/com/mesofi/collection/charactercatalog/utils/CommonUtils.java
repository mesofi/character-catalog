/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public static BigDecimal toPrice(final String value) {
        if (StringUtils.hasText(value)) {
            return new BigDecimal(value.substring(1).replace(',', '.'));
        }
        return null;
    }

    public static LocalDate toDate(final String value) {
        if (StringUtils.hasText(value)) {
            // String format = isDayConfirmed(value) ? "M/d/yyyy" : "M/yyyy";
            // Boolean isConfirmed = isDayConfirmed(value);
            // if (Objects.nonNull(isConfirmed)) {
            // String format = isConfirmed ? "[MM][M]/d/yyyy" : "[MM][M]/yyyy";
            // System.out.println(value);
            // System.out.println(format);

            // DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            // .appendOptional(DateTimeFormatter.ofPattern(format)).toFormatter();

            // return LocalDate.parse(value, formatter);
            return null;
        }
        return null;
    }
}
