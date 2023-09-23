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
 * The actual value object used to hold the info.
 *
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@EqualsAndHashCode
public class CharacterFigure {

    private String originalName; // Name of the character.

    @EqualsAndHashCode.Exclude
    private String baseName; // Base name of the character.

    @EqualsAndHashCode.Exclude
    private BigDecimal basePrice; // The price without taxes.

    @EqualsAndHashCode.Exclude
    private LocalDate firstAnnouncementDate; // Date when the figure was first announced.

    @EqualsAndHashCode.Exclude
    private LocalDate preorderDate; // Date when the figure was set to pre-order.

    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate; // Date when the figure was release.
}
