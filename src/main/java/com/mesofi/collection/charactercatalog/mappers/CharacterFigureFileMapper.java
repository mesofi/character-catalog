/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toBoolean;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toDate;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toInteger;
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
        characterFigure.setUrl(columns[7]);
        characterFigure.setDistribution(toEnum(columns[8], Distribution.class));
        characterFigure.setLineUp(toEnum(columns[9], LineUp.class));
        characterFigure.setSeries(toEnum(columns[10], Series.class));
        characterFigure.setGroup(toEnum(columns[11], Group.class));
        characterFigure.setMetalBody(toBoolean(columns[12]));
        characterFigure.setOce(toBoolean(columns[13]));
        characterFigure.setRevival(toBoolean(columns[14]));
        characterFigure.setPlainCloth(toBoolean(columns[16]));
        characterFigure.setBrokenCloth(toBoolean(columns[17]));
        characterFigure.setBrozeToGold(toBoolean(columns[18]));
        characterFigure.setGold(toBoolean(columns[19]));
        characterFigure.setHongKongVersion(toBoolean(columns[20]));
        characterFigure.setManga(toBoolean(columns[21]));
        characterFigure.setSurplice(toBoolean(columns[22]));
        characterFigure.setSet(toBoolean(columns[23]));
        if (columns.length >= 25) {
            characterFigure.setAnniversary(toInteger(columns[24]));
        }
        if (columns.length >= 26) {
            characterFigure.setRemarks(columns[25]);
        }

        return characterFigure;
    }

    @SuppressWarnings("unchecked")
    private <T> T toEnum(String value, Class<?> clazz) {
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
