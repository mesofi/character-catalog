/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

/**
 * The Series enum.
 *
 * @author armandorivasarzaluz
 */
@Getter
public enum Series {

    // @formatter:off
    SAINT_SEIYA("Saint Seiya"),
    SAINTIA_SHO("Saintia Sho"),
    SOG("Soul of Gold"),
    LEGEND("Legend Of Sanctuary"),
    OMEGA("Omega"),
    LOST_CANVAS("Lost Canvas");
    // @formatter:on

    private final String stringValue;

    Series(String friendlyName) {
        this.stringValue = friendlyName;
    }

    public String toString() {
        return this.stringValue;
    }
}
