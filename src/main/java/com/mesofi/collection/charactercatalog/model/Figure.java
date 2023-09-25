/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public abstract class Figure {

    @EqualsAndHashCode.Exclude
    private BigDecimal basePrice; // The price without taxes.

    @EqualsAndHashCode.Exclude
    private BigDecimal releasePrice; // <== Calculated == The price with taxes.

    @EqualsAndHashCode.Exclude
    private LocalDate firstAnnouncementDate; // Date when the figure was first announced.

    @EqualsAndHashCode.Exclude
    private LocalDate preorderDate; // Date when the figure was set to pre-order.

    @EqualsAndHashCode.Exclude
    private Boolean preorderConfirmationDay; // <== Calculated == Used to determine if the day was confirmed.

    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate; // Date when the figure was released in Japan.

    @EqualsAndHashCode.Exclude
    private Boolean releaseConfirmationDay; // <== Calculated == Used to determine if the day was confirmed.

    @EqualsAndHashCode.Exclude
    private String url; // URL for the Tamashii website.

    @EqualsAndHashCode.Exclude
    private Distribution distribution; // how this figure was distributed.

    @EqualsAndHashCode.Exclude
    private String remarks; // remarks or comments.
}
