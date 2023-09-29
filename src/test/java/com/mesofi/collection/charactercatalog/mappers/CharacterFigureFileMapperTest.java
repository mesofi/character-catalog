/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;

/**
 * Test for {@link CharacterFigureFileMapper}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFigureFileMapperTest {

    private CharacterFigureFileMapper characterFigureFileMapper;

    @BeforeEach
    public void init() {
        characterFigureFileMapper = new CharacterFigureFileMapper();
    }

    @Test
    public void should_map_null_character() {
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(null);
        assertNull(figure);
    }

    @Test
    public void should_map_character() {
        String line = "Dragon Shiryu ~New Bronze Cloth~\tDragon Shiryu\t¥6,000\t¥6,600\t\t\t8/24/2013\t\t\t\thttps://tamashiiweb.com/item/10372\tStores\tMyth Cloth EX\tSaint Seiya\tBronze Saint V2\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertEquals("Dragon Shiryu ~New Bronze Cloth~", figure.getOriginalName());
        assertEquals("Dragon Shiryu", figure.getBaseName());
    }
}
