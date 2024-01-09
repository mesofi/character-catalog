/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 23, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link Series}
 * 
 * @author armandorivasarzaluz
 */
public class SeriesTest {

    //@Test
    public void should_return_series_names() {
        assertEquals("Saint Seiya", Series.SAINT_SEIYA.getDescription());
        assertEquals("Soul of Gold", Series.SOG.getDescription());
        assertEquals("Saint Seiya Omega", Series.OMEGA.getDescription());
        assertEquals("Saintia Sho", Series.SAINTIA_SHO.getDescription());
        assertEquals("Saint Seiya Legend Of Sanctuary", Series.LEGEND.getDescription());
        assertEquals("The Lost Canvas", Series.LOST_CANVAS.getDescription());
        assertEquals("Saint Seiya The Beginning", Series.THE_BEGINNING.getDescription());

        assertEquals("Saint Seiya", Series.SAINT_SEIYA.toString());
        assertEquals("Soul of Gold", Series.SOG.toString());
        assertEquals("Saint Seiya Omega", Series.OMEGA.toString());
        assertEquals("Saintia Sho", Series.SAINTIA_SHO.toString());
        assertEquals("Saint Seiya Legend Of Sanctuary", Series.LEGEND.toString());
        assertEquals("The Lost Canvas", Series.LOST_CANVAS.toString());
        assertEquals("Saint Seiya The Beginning", Series.THE_BEGINNING.toString());
    }
}
