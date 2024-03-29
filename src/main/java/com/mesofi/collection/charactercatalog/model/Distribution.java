/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

/**
 * The Distribution enum.
 *
 * @author armandorivasarzaluz
 */
@Getter
public enum Distribution {

    // @formatter:off
    STORES("Stores"),
    TAMASHII_WEB_SHOP("Tamashii Web Shop"),
    TAMASHII_WORLD_TOUR("Tamashii World Tour"),
    TAMASHII_NATIONS("Tamashii Nations"),
    TAMASHII_STORE("Tamashii Store"),
    OTHER("Other Limited Edition");
    // @formatter:on

    private final String stringValue;

    Distribution(String friendlyName) {
        this.stringValue = friendlyName;
    }

    public String toString() {
        return this.stringValue;
    }
}
