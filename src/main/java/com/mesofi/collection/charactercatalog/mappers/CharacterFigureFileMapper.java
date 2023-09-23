/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toDate;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toPrice;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

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
        characterFigure.setBasePrice(toPrice(columns[2]));
        characterFigure.setFirstAnnouncementDate(toDate(columns[4]));
        characterFigure.setPreorderDate(toDate(columns[5]));
        characterFigure.setReleaseDate(toDate(columns[6]));
        characterFigure.setTamashiiUrl(columns[7]);
        characterFigure.setDistribution(toValues(columns[8], Distribution.class));
        characterFigure.setLineUp(toValues(columns[9], LineUp.class));
        characterFigure.setSeries(toValues(columns[10], Series.class));
        characterFigure.setGroup(toValues(columns[11], Group.class));

        return characterFigure;
    }

    @SuppressWarnings("unchecked")
    private <T> T toValues(String value, Class<?> clazz) {
        if (clazz.isEnum()) {
            Object[] constants = clazz.getEnumConstants();
            for (Object object : constants) {
                Enum<?> e = (Enum<?>) object;
                if (e.toString().equals(value)) {
                    return (T) e;
                }
            }
        }
        return null;
    }
}
