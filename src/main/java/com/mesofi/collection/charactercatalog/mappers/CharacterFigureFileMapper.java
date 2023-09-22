/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;

/**
 * The actual Character Figure file mapper.
 *
 * @author armandorivasarzaluz
 */
@Component
public class CharacterFigureFileMapper {

    /**
     * Converts a plain line to a character figure object.
     * 
     * @param line The line to be parsed and converted.
     * @return The character figure.
     */
    public CharacterFigure fromLineToCharacterFigure(final String line) {
        if (!StringUtils.hasText(line)) {
            return null;
        }

        String[] columns = line.split("\t");
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setOriginalName(columns[0]);
        characterFigure.setBaseName(columns[1]);
        return characterFigure;
    }
}
