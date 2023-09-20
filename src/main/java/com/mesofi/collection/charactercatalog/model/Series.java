/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

@Getter
public enum Series {
    // @formatter:off
    SAINT_SEIYA("Saint Seiya"),
    SAINTIA_SHO("Saintia Sho"),
    SOG("Soul of Gold"),
    LEGEND("Legend Of Sanctuary"),
    OMEGA("Omega"),
    LL("Lost Canvas");
    // @formatter:on

    private final String friendlyName;

    Series(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String toString() {
        return this.friendlyName;
    }
}
