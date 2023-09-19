/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CharacterFigure extends Figure {
    @Id
    public String id;
    private String baseName; // Name of the character
    private String displayedName; // Names to be displayed based on attributes of the figure.

    private LineUp lineUp; // MythCloth ... MythCloth EX etc.
    private Series series = Series.SAINT_SEIYA; // Saint Seiya
    private Group group = Group.OTHER; // This figure belongs to a certain group

    private boolean metalBody;
    private boolean oce;
    private boolean revival;
    private boolean plainCloth;
    private boolean broken;
    private boolean golden;
    private boolean gold;
    private boolean hk;
    private boolean manga;
    private boolean surplice;
    private boolean set;
    private Integer anniversary;
    private List<RestockFigure> restocks;
}
