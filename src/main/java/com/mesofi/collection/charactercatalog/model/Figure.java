/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Figure {

    @EqualsAndHashCode.Exclude
    private BigDecimal basePrice; // Base price without taxes.
    @EqualsAndHashCode.Exclude
    private BigDecimal officialPrice; // Price with taxes.

    @EqualsAndHashCode.Exclude
    private Date firstAnnouncementDate; // Date when the figure was first announced.
    @EqualsAndHashCode.Exclude
    private Boolean preOrderConfirmedDayDate;
    @EqualsAndHashCode.Exclude
    private Date preOrderDate; // The preOrder date.
    @EqualsAndHashCode.Exclude
    private Boolean releaseConfirmedDayDate;
    @EqualsAndHashCode.Exclude
    private Date releaseDate; // The release date in Japan

    @EqualsAndHashCode.Exclude
    private Distribution distribution; // how this figure was
    @EqualsAndHashCode.Exclude
    private String url; // URL for the Tamashii website.

    @EqualsAndHashCode.Exclude
    private String remarks;
}
