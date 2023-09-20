/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

/**
 * 
 */
@Getter
public enum Group {
    // @formatter:off
    V1("Bronze Saint V1"),
    V2("Bronze Saint V2"),
    V3("Bronze Saint V3"),
    V4("Bronze Saint V4"),
    V5("Bronze Saint V5"),
    SECONDARY("Bronze Secondary"),
    BLACK("Black Saint"),
    STEEL("Steel"),
    SILVER("Silver Saint"),
    GOLD("Gold Saint"),
    ROBE("God Robe"),
    SCALE("Poseidon Scale"),
    SURPLICE("Saint Surplice"),
    SPECTER("Specter"),
    JUDGE("Judge"),
    GOD("God"),
    OTHER("-");
    // @formatter:on

    private final String friendlyName;

    Group(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String toString() {
        return this.friendlyName;
    }
}
