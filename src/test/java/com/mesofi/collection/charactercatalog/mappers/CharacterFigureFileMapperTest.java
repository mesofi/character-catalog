/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

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

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    @Test
    public void should_map_null_character() {
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(null);
        assertNull(figure);
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    @Test
    public void should_map_character_simple() {
        String line = "Epsilon Alioth Fenril\tEpsilon Alioth Fenril\t¥0\t¥0\t\t\t\t\t\t\t\t\tMyth Cloth EX\tSaint Seiya\tGod Robe\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("0"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertNull(figure.getIssuanceJPY().getPreorderDate());
        assertNull(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertNull(figure.getIssuanceJPY().getReleaseDate());
        assertNull(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertTrue(figure.isFutureRelease());
        assertNull(figure.getUrl());
        assertNull(figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Epsilon Alioth Fenril", figure.getOriginalName());
        assertEquals("Epsilon Alioth Fenril", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.ROBE, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    @Test
    public void should_map_character_complete_1() {
        String line = "Dragon Shiryu ~New Bronze Cloth~\tDragon Shiryu\t¥6,000\t¥6,600\t\t\t8/24/2013\t\t\t\thttps://tamashiiweb.com/item/10372\tStores\tMyth Cloth EX\tSaint Seiya\tBronze Saint V2\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("6.000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertNull(figure.getIssuanceJPY().getPreorderDate());
        assertNull(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2013, 8, 24), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/10372", figure.getUrl());
        assertEquals(Distribution.STORES, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Dragon Shiryu ~New Bronze Cloth~", figure.getOriginalName());
        assertEquals("Dragon Shiryu", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.V2, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertTrue(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    @Test
    public void should_map_character_complete_2() {
        String line = "Phoenix Ikki ~Initial Bronze Cloth 20th Anniversary Ver.~\tPhoenix Ikki\t\t\t\t\t\t\t\t\t\t\tMyth Cloth\tSaint Seiya\tBronze Saint V1\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t20\t20th anniversary";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNull(figure.getIssuanceJPY());
        assertNull(figure.getIssuanceMXN());
        assertTrue(figure.isFutureRelease());
        assertNull(figure.getUrl());
        assertNull(figure.getDistribution());
        assertEquals("20th anniversary", figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Phoenix Ikki ~Initial Bronze Cloth 20th Anniversary Ver.~", figure.getOriginalName());
        assertEquals("Phoenix Ikki", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.V1, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertEquals(20, figure.getAnniversary());
        assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    @Test
    public void should_map_character_complete_3() {
        String line = "Phoenix Ikki ~Initial Bronze Cloth 20th Anniversary Ver.~\tPhoenix Ikki\t\t\t\t\t\t\t\t\t\t\tMyth Cloth\tSaint Seiya\tBronze Saint V1\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t20\t20th anniversary\tmilo,gold,revival";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNull(figure.getIssuanceJPY());
        assertNull(figure.getIssuanceMXN());
        assertTrue(figure.isFutureRelease());
        assertNull(figure.getUrl());
        assertNull(figure.getDistribution());
        assertEquals("20th anniversary", figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Phoenix Ikki ~Initial Bronze Cloth 20th Anniversary Ver.~", figure.getOriginalName());
        assertEquals("Phoenix Ikki", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.V1, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertEquals(20, figure.getAnniversary());
        assertNull(figure.getRestocks());
        assertNotNull(figure.getTags());
        assertEquals(List.of("gold", "revival", "milo"), new ArrayList<>(figure.getTags()));
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    @Test
    public void should_create_empty_issuance_when_input_is_null() {
        assertNull(characterFigureFileMapper.createIssuance(null));
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    @Test
    public void should_create_empty_issuance_when_input_is_empty() {
        Issuance issuance = new Issuance();
        assertNull(characterFigureFileMapper.createIssuance(issuance));
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    @Test
    public void should_create_issuance_when_input_is_provided() {
        Issuance issuance = new Issuance();
        issuance.setBasePrice(new BigDecimal("34.4"));
        Issuance actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(new BigDecimal("34.4"), actual.getBasePrice());

        issuance = new Issuance();
        issuance.setFirstAnnouncementDate(LocalDate.of(2023, 3, 3));
        actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2023, 3, 3), actual.getFirstAnnouncementDate());

        issuance = new Issuance();
        issuance.setPreorderDate(LocalDate.of(2023, 10, 10));
        actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2023, 10, 10), actual.getPreorderDate());

        issuance = new Issuance();
        issuance.setReleaseDate(LocalDate.of(2023, 10, 10));
        actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2023, 10, 10), actual.getReleaseDate());
    }
}
