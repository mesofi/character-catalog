package com.mesofi.collection.charactercatalog.ui;

import java.math.BigDecimal;
import java.util.Locale;

public class Test {
    public static void main(String[] args) {

        Locale locale = new Locale("jp", "JP");
        BigDecimal bd = new BigDecimal("35.20000");
        BigDecimal bd1 = new BigDecimal(33454);
        System.out.println(bd);
        System.out.println(bd1);
        System.out.println(String.format(locale, "%,.0f", bd));

    }
}
