/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.INVALID_BASE_NAME;
import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.INVALID_GROUP;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The actual value object used to hold the character info.
 * 
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class CharacterFigure extends Figure {

    @EqualsAndHashCode.Exclude
    private String id; // identifier of the record.

    @EqualsAndHashCode.Exclude
    private String originalName; // Name of the character.

    @NotBlank(message = INVALID_BASE_NAME)
    private String baseName; // Base name of the character.

    @EqualsAndHashCode.Exclude
    private String displayableName; // <== Calculated == name to be displayed.

    private LineUp lineUp; // MythCloth ... MythCloth EX etc.

    private Series series; // Saint Seiya, Lost Canvas etc.

    @NotNull(message = INVALID_GROUP)
    private Group group; // Group of the character.

    private boolean metalBody; // Has a metal body.

    private boolean oce; // Is Original Color Edition?.

    private boolean revival; // Is revival?.

    private boolean plainCloth; // Is plain cloth?.

    @EqualsAndHashCode.Exclude
    private boolean brokenCloth; // Contains broken armor parts?.

    private boolean bronzeToGold; // Does the bronze cloth become gold?.

    private boolean gold; // Contains true gold?.

    private boolean hongKongVersion; // Is it HK version?.

    private boolean manga; // Is it manga version?.

    private boolean surplice; // Is it a surplice?.

    @EqualsAndHashCode.Exclude
    private boolean set; // Is it part of a set?.

    private Integer anniversary; // Is it part of an anniversary?.

    // @EqualsAndHashCode.Exclude
    // private List<RestockFigure> restocks; // used to store the restocks.

    @EqualsAndHashCode.Exclude
    private Set<String> tags; // the list of tags associated to a character.

    @EqualsAndHashCode.Exclude
    private List<GalleryImage> images; // list of images associated to this character.

}
