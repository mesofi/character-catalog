/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

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
}
