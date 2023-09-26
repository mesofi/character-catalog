/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The actual value object used to hold the info.
 *
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CharacterFigure extends Figure {

    private String originalName; // Name of the character.

    @EqualsAndHashCode.Exclude
    private String baseName; // Base name of the character.

    @EqualsAndHashCode.Exclude
    private String displayableName; // <== Calculated == name to be displayed.

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
    private boolean bronzeToGold; // Does the bronze cloth become gold?.

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
    private List<RestockFigure> restocks;
}
