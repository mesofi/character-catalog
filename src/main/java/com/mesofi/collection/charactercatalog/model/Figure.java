/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Common properties.
 *
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Figure {

    @EqualsAndHashCode.Exclude
    private Issuance issuanceJPY;

    @EqualsAndHashCode.Exclude
    private Issuance issuanceMXN;

    @EqualsAndHashCode.Exclude
    private boolean futureRelease; // <== Calculated == Used to determine if the figure is in the future or not.

    @EqualsAndHashCode.Exclude
    private String url; // URL for the Tamashii website.

    @EqualsAndHashCode.Exclude
    private Distribution distribution; // how this figure was distributed.

    @EqualsAndHashCode.Exclude
    private String remarks; // remarks or comments.
}
