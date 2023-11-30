/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import java.util.List;
import java.util.Objects;

/**
 * Common utilities.
 * 
 * @author armandorivasarzaluz
 */
public class CommonUtils {

    private CommonUtils() {

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
