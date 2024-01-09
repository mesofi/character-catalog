/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * 
 * Test for {@link Distribution}
 *
 * @author armandorivasarzaluz
 */
public class DistributionTest {

    //@Test
    public void should_return_distribution_names() {
        assertEquals("Stores", Distribution.STORES.getDescription());
        assertEquals("Tamashii Web Shop", Distribution.TAMASHII_WEB_SHOP.getDescription());
        assertEquals("Tamashii World Tour", Distribution.TAMASHII_WORLD_TOUR.getDescription());
        assertEquals("Tamashii Nations", Distribution.TAMASHII_NATIONS.getDescription());
        assertEquals("Tamashii Store", Distribution.TAMASHII_STORE.getDescription());
        assertEquals("Other Limited Edition", Distribution.OTHER.getDescription());

        assertEquals("Stores", Distribution.STORES.toString());
        assertEquals("Tamashii Web Shop", Distribution.TAMASHII_WEB_SHOP.toString());
        assertEquals("Tamashii World Tour", Distribution.TAMASHII_WORLD_TOUR.toString());
        assertEquals("Tamashii Nations", Distribution.TAMASHII_NATIONS.toString());
        assertEquals("Tamashii Store", Distribution.TAMASHII_STORE.toString());
        assertEquals("Other Limited Edition", Distribution.OTHER.toString());
    }
}
