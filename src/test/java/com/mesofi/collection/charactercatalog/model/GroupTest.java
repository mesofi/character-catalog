/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 24, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link Group}
 */
public class GroupTest {

    @Test
    public void should_return_group_names() {
        assertEquals("Bronze Saint V1", Group.V1.getDescription());
        assertEquals("Bronze Saint V2", Group.V2.getDescription());
        assertEquals("Bronze Saint V3", Group.V3.getDescription());
        assertEquals("Bronze Saint V4", Group.V4.getDescription());
        assertEquals("Bronze Saint V5", Group.V5.getDescription());
        assertEquals("Bronze Secondary", Group.SECONDARY.getDescription());
        assertEquals("Black Saint", Group.BLACK.getDescription());
        assertEquals("Steel", Group.STEEL.getDescription());
        assertEquals("Silver Saint", Group.SILVER.getDescription());
        assertEquals("Gold Saint", Group.GOLD.getDescription());
        assertEquals("God Robe", Group.ROBE.getDescription());
        assertEquals("Poseidon Scale", Group.SCALE.getDescription());
        assertEquals("Surplice Saint", Group.SURPLICE.getDescription());
        assertEquals("Specter", Group.SPECTER.getDescription());
        assertEquals("Judge", Group.JUDGE.getDescription());
        assertEquals("God", Group.GOD.getDescription());
        assertEquals("-", Group.OTHER.getDescription());
    }
}
