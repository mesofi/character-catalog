/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

public enum Series {
    // @formatting:off
    SAINT_SEIYA("Saint Seiya"), SAINTIA_SHO("Saintia Sho"), SOG("Soul of Gold"), LEYEND("Legend Of Santuary"),
    OMEGA("Omega"), LL("Lost Canvas");
    // @formatting:on

    private final String friendlyName;

    Series(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String toString() {
        return this.friendlyName;
    }
}
