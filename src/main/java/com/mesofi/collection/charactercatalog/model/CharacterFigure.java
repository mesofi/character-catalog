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
    private LocalDate releaseDate; // Date when the figure was released.

    @EqualsAndHashCode.Exclude
    private String tamashiiUrl; // URL for the Tamashii website.

    @EqualsAndHashCode.Exclude
    private Distribution distribution; // how this figure was distributed.

    private LineUp lineUp; // MythCloth ... MythCloth EX etc.

    private Series series; // Saint Seiya, Lost Canvas etc.

    private Group group; // Group of the character.

    private boolean metalBody; // Has a metal body.

    private boolean oce; // Is Original Color Edition?.

    private boolean revival; // Is revival?.

    private boolean plainCloth; // Is plain cloth?.

    @EqualsAndHashCode.Exclude
    private boolean brokenCloth; // Contains broken armor parts?.

    @EqualsAndHashCode.Exclude
    private boolean brozeToGold; // Does the bronze cloth become gold?.

    @EqualsAndHashCode.Exclude
    private boolean gold; // Contains true gold?.

    private boolean hongKongVersion; // Is it HK version?.

    private boolean manga; // Is it manga version?.

    private boolean surplice; // Is it a surplice?.

    @EqualsAndHashCode.Exclude
    private boolean set; // Is it part of a set?.

    @EqualsAndHashCode.Exclude
    private Integer anniversary; // Is it part of an anniversary?.

    @EqualsAndHashCode.Exclude
    private String remarks; // remarks or comments.

}
