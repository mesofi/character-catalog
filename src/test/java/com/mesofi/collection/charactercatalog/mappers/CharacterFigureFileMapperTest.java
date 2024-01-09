/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Dec 1, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
 */
public class CharacterFigureFileMapperTest {

    private CharacterFigureFileMapper characterFigureFileMapper;

    @BeforeEach
    public void init() {
        characterFigureFileMapper = new CharacterFigureFileMapper();
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_null_character() {
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(null);
        assertNull(figure);
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_unreleased_character() {
        String line = "Epsilon Alioth Fenril EX\tEpsilon Alioth Fenril\t¥0\t¥0\t\t\t\t\t\t\t\t\tMyth Cloth EX\tSaint Seiya\tGod Robe\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t\t";
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
        assertEquals("Epsilon Alioth Fenril EX", figure.getOriginalName());
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
        assertNotNull(figure.getImages());
        assertNotNull(figure.getImages().get(0));
        assertTrue(figure.getImages().get(0).isCoverPhoto());
        assertFalse(figure.getImages().get(0).isOfficial());
        assertNull(figure.getImages().get(0).getIdImage());
        assertEquals(1, figure.getImages().get(0).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/923/3hbcya.png",
                figure.getImages().get(0).getUrl());
        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_released_character_mxn_and_jpy() {
        String line = "Scorpio Milo EX <Revival>\tScorpio Milo\t¥12,000\t¥13,200\t4/10/2023\t4/26/2023\t9/16/2023\t$2,300\t9/26/2023\t11/6/2023\thttps://tamashiiweb.com/item/14387\tStores\tMyth Cloth EX\tSaint Seiya\tGold Saint\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t923/vMiqIK\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("12000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertEquals(LocalDate.of(2023, 4, 10), figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 4, 26), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 9, 16), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNotNull(figure.getIssuanceMXN());
        assertEquals(new BigDecimal("2300"), figure.getIssuanceMXN().getBasePrice());
        assertNull(figure.getIssuanceMXN().getReleasePrice());
        assertNull(figure.getIssuanceMXN().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 9, 26), figure.getIssuanceMXN().getPreorderDate());
        assertTrue(figure.getIssuanceMXN().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 11, 6), figure.getIssuanceMXN().getReleaseDate());
        assertTrue(figure.getIssuanceMXN().getReleaseConfirmationDay());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/14387", figure.getUrl());
        assertEquals(Distribution.STORES, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Scorpio Milo EX <Revival>", figure.getOriginalName());
        assertEquals("Scorpio Milo", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertTrue(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_released_character_with_custom_values() {
        String line = "Scorpio Milo EX <Revival>\tScorpio Milo\t¥12,000\t¥13,200\t4/10/2023\t4/26/2023\t9/16/2023\t$2,300\t9/26/2023\t11/6/2023\thttps://tamashiiweb.com/item/14387\tStores\tMyth Cloth EX\tSaint Seiya\tGold Saint\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\tTRUE\t\t\t\t923/vMiqIK\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("12000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertEquals(LocalDate.of(2023, 4, 10), figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 4, 26), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 9, 16), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNotNull(figure.getIssuanceMXN());
        assertEquals(new BigDecimal("2300"), figure.getIssuanceMXN().getBasePrice());
        assertNull(figure.getIssuanceMXN().getReleasePrice());
        assertNull(figure.getIssuanceMXN().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 9, 26), figure.getIssuanceMXN().getPreorderDate());
        assertTrue(figure.getIssuanceMXN().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 11, 6), figure.getIssuanceMXN().getReleaseDate());
        assertTrue(figure.getIssuanceMXN().getReleaseConfirmationDay());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/14387", figure.getUrl());
        assertEquals(Distribution.STORES, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Scorpio Milo EX <Revival>", figure.getOriginalName());
        assertEquals("Scorpio Milo", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
        assertTrue(figure.isMetalBody());
        assertTrue(figure.isOce());
        assertTrue(figure.isRevival());
        assertTrue(figure.isPlainCloth());
        assertTrue(figure.isBrokenCloth());
        assertTrue(figure.isBronzeToGold());
        assertTrue(figure.isGold());
        assertTrue(figure.isHongKongVersion());
        assertTrue(figure.isManga());
        assertTrue(figure.isSurplice());
        assertTrue(figure.isSet());
        assertNull(figure.getAnniversary());
        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_released_character_with_28_columns() {
        String line = "Pegasus Cross Object JUMP 50th ANNIVERSARY EDITION <Appendix> (Restock)\tPegasus Cross Object\t¥1,389\t¥1,528\t\t11/20/2017\t11/20/2017\t\t\t\thttps://tamashiiweb.com/item/12413\tTamashii Web Shop\tAppendix\tSaint Seiya\t-\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t50\t\t\t\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("1389"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2017, 11, 20), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2017, 11, 20), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/12413", figure.getUrl());
        assertEquals(Distribution.TAMASHII_WEB_SHOP, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Pegasus Cross Object JUMP 50th ANNIVERSARY EDITION <Appendix> (Restock)",
                figure.getOriginalName());
        assertEquals("Pegasus Cross Object", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.APPENDIX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.OTHER, figure.getGroup());
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
        assertEquals(50, figure.getAnniversary());
        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_released_character_with_29_columns() {
        String line = "Andromeda Shun ~New Bronze Cloth~ Golden Limited Edition EX (Restock)\tAndromeda Shun\t¥8,000\t¥8,800\t\t10/30/2020\t12/2020\t\t\t\thttps://tamashiiweb.com/item/13474\tTamashii Web Shop\tMyth Cloth EX\tSaint Seiya\tBronze Saint V2\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\tOnline Special Sale\t\t\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("8000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2020, 10, 30), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2020, 12, 1), figure.getIssuanceJPY().getReleaseDate());
        assertFalse(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/13474", figure.getUrl());
        assertEquals(Distribution.TAMASHII_WEB_SHOP, figure.getDistribution());
        assertEquals("Online Special Sale", figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Andromeda Shun ~New Bronze Cloth~ Golden Limited Edition EX (Restock)", figure.getOriginalName());
        assertEquals("Andromeda Shun", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.V2, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertTrue(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#fromLineToCharacterFigure(String)}
     */
    //@Test
    public void should_map_released_character_with_30_columns() {
        String line = "Pisces Aphrodite (God Cloth) EX (Restock)\tPisces Aphrodite\t¥12,500\t¥13,750\t\t\t8/19/2019\t\t\t\thttps://tamashiiweb.com/item/12177\tStores\tMyth Cloth EX\tSoul of Gold\tGold Saint\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\tre-stock\t\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("12500"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertNull(figure.getIssuanceJPY().getPreorderDate());
        assertNull(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2019, 8, 19), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/12177", figure.getUrl());
        assertEquals(Distribution.STORES, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Pisces Aphrodite (God Cloth) EX (Restock)", figure.getOriginalName());
        assertEquals("Pisces Aphrodite", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SOG, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
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
        assertEquals(Set.of("re-stock"), figure.getTags());
        // assertNull(figure.getRestocks());
    }

    //@Test
    public void should_map_released_character_with_31_columns() {
        String line = "Sagittarius Seiya Gold 24K EX\tSagittarius Seiya Gold 24K EX\t¥20,000\t¥22,000\t\t9/17/2020\t11/4/2020\t\t\t\thttps://tamashiiweb.com/item/13412\tTamashii Nations\tMyth Cloth EX\tSaint Seiya\tGold Saint\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\t\tTN 2020\tgold24/tamashii\t923/EdIHV5\t";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("20000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2020, 9, 17), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2020, 11, 4), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/13412", figure.getUrl());
        assertEquals(Distribution.TAMASHII_NATIONS, figure.getDistribution());
        assertEquals("TN 2020", figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Sagittarius Seiya Gold 24K EX", figure.getOriginalName());
        assertEquals("Sagittarius Seiya Gold 24K EX", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertTrue(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        assertEquals(Set.of("gold24/tamashii"), figure.getTags());
        assertNotNull(figure.getImages());
        assertEquals(1, figure.getImages().size());
        // assertEquals(Set.of("gold24/tamashii"), figure.getImages().get(0).get);
        // assertNull(figure.getRestocks());
    }

    //@Test
    public void should_map_released_character_with_32_columns() {
        String line = "Sagittarius Seiya Gold 24K EX\tSagittarius Seiya Gold 24K EX\t¥20,000\t¥22,000\t\t9/17/2020\t11/4/2020\t\t\t\thttps://tamashiiweb.com/item/13412\tTamashii Nations\tMyth Cloth EX\tSaint Seiya\tGold Saint\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\t\tTN 2020\tgold24/tamashii\t923/EdIHV5\t923/AsswrAw";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("20000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertNull(figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2020, 9, 17), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2020, 11, 4), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(figure.getIssuanceMXN());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/13412", figure.getUrl());
        assertEquals(Distribution.TAMASHII_NATIONS, figure.getDistribution());
        assertEquals("TN 2020", figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Sagittarius Seiya Gold 24K EX", figure.getOriginalName());
        assertEquals("Sagittarius Seiya Gold 24K EX", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertFalse(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertTrue(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        assertEquals(Set.of("gold24/tamashii"), figure.getTags());
        assertNotNull(figure.getImages());
        assertEquals(2, figure.getImages().size());

        assertNotNull(figure.getImages().get(0));
        assertTrue(figure.getImages().get(0).isCoverPhoto());
        assertTrue(figure.getImages().get(0).isOfficial());
        assertNull(figure.getImages().get(0).getIdImage());
        assertEquals(1, figure.getImages().get(0).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/923/EdIHV5.jpg",
                figure.getImages().get(0).getUrl());

        assertNotNull(figure.getImages().get(1));
        assertFalse(figure.getImages().get(1).isCoverPhoto());
        assertFalse(figure.getImages().get(1).isOfficial());
        assertNotNull(figure.getImages().get(1).getIdImage());
        assertEquals(2, figure.getImages().get(1).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/923/AsswrAw.jpg",
                figure.getImages().get(1).getUrl());
        // assertEquals(Set.of("gold24/tamashii"), figure.getImages().get(0).get);
        // assertNull(figure.getRestocks());
    }

    //@Test
    public void should_map_released_character_with_multiple_official_and_non_official_images() {
        String line = "Scorpio Milo EX <Revival>\tScorpio Milo\t¥12,000\t¥13,200\t4/10/2023\t4/26/2023\t9/16/2023\t$2,300\t9/26/2023\t11/6/2023\thttps://tamashiiweb.com/item/14387\tStores\tMyth Cloth EX\tSaint Seiya\tGold Saint\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t923/vMiqIK,924/mqXD5s\t924/dUsdfe9f,924/dUsdfe8f,924/dUsdfe7f";
        CharacterFigure figure = characterFigureFileMapper.fromLineToCharacterFigure(line);
        assertNotNull(figure.getIssuanceJPY());
        assertEquals(new BigDecimal("12000"), figure.getIssuanceJPY().getBasePrice());
        assertNull(figure.getIssuanceJPY().getReleasePrice());
        assertEquals(LocalDate.of(2023, 4, 10), figure.getIssuanceJPY().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 4, 26), figure.getIssuanceJPY().getPreorderDate());
        assertTrue(figure.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 9, 16), figure.getIssuanceJPY().getReleaseDate());
        assertTrue(figure.getIssuanceJPY().getReleaseConfirmationDay());
        assertNotNull(figure.getIssuanceMXN());
        assertEquals(new BigDecimal("2300"), figure.getIssuanceMXN().getBasePrice());
        assertNull(figure.getIssuanceMXN().getReleasePrice());
        assertNull(figure.getIssuanceMXN().getFirstAnnouncementDate());
        assertEquals(LocalDate.of(2023, 9, 26), figure.getIssuanceMXN().getPreorderDate());
        assertTrue(figure.getIssuanceMXN().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 11, 6), figure.getIssuanceMXN().getReleaseDate());
        assertTrue(figure.getIssuanceMXN().getReleaseConfirmationDay());
        assertFalse(figure.isFutureRelease());
        assertEquals("https://tamashiiweb.com/item/14387", figure.getUrl());
        assertEquals(Distribution.STORES, figure.getDistribution());
        assertNull(figure.getRemarks());
        assertNull(figure.getId());
        assertEquals("Scorpio Milo EX <Revival>", figure.getOriginalName());
        assertEquals("Scorpio Milo", figure.getBaseName());
        assertNull(figure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, figure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, figure.getSeries());
        assertEquals(Group.GOLD, figure.getGroup());
        assertFalse(figure.isMetalBody());
        assertFalse(figure.isOce());
        assertTrue(figure.isRevival());
        assertFalse(figure.isPlainCloth());
        assertFalse(figure.isBrokenCloth());
        assertFalse(figure.isBronzeToGold());
        assertFalse(figure.isGold());
        assertFalse(figure.isHongKongVersion());
        assertFalse(figure.isManga());
        assertFalse(figure.isSurplice());
        assertFalse(figure.isSet());
        assertNull(figure.getAnniversary());
        assertNull(figure.getTags());
        assertNotNull(figure.getImages());
        assertEquals(5, figure.getImages().size());

        assertNotNull(figure.getImages().get(0));
        assertTrue(figure.getImages().get(0).isCoverPhoto());
        assertTrue(figure.getImages().get(0).isOfficial());
        assertNull(figure.getImages().get(0).getIdImage());
        assertEquals(1, figure.getImages().get(0).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/923/vMiqIK.jpg",
                figure.getImages().get(0).getUrl());

        assertNotNull(figure.getImages().get(1));
        assertFalse(figure.getImages().get(1).isCoverPhoto());
        assertTrue(figure.getImages().get(1).isOfficial());
        assertNull(figure.getImages().get(1).getIdImage());
        assertEquals(2, figure.getImages().get(1).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/924/mqXD5s.jpg",
                figure.getImages().get(1).getUrl());

        assertNotNull(figure.getImages().get(2));
        assertFalse(figure.getImages().get(2).isCoverPhoto());
        assertFalse(figure.getImages().get(2).isOfficial());
        assertNotNull(figure.getImages().get(2).getIdImage());
        assertEquals(3, figure.getImages().get(2).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/924/dUsdfe9f.jpg",
                figure.getImages().get(2).getUrl());

        assertNotNull(figure.getImages().get(3));
        assertFalse(figure.getImages().get(3).isCoverPhoto());
        assertFalse(figure.getImages().get(3).isOfficial());
        assertNotNull(figure.getImages().get(3).getIdImage());
        assertEquals(4, figure.getImages().get(3).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/924/dUsdfe8f.jpg",
                figure.getImages().get(3).getUrl());

        assertNotNull(figure.getImages().get(4));
        assertFalse(figure.getImages().get(4).isCoverPhoto());
        assertFalse(figure.getImages().get(4).isOfficial());
        assertNotNull(figure.getImages().get(4).getIdImage());
        assertEquals(5, figure.getImages().get(4).getOrder());
        assertEquals("https://imagizer.imageshack.com/v2/320x240q70/924/dUsdfe7f.jpg",
                figure.getImages().get(4).getUrl());

        // assertNull(figure.getRestocks());
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    //@Test
    public void should_create_empty_issuance_when_input_is_null() {
        assertNull(characterFigureFileMapper.createIssuance(null));
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    //@Test
    public void should_create_empty_issuance_when_input_is_empty() {
        Issuance issuance = new Issuance();
        assertNull(characterFigureFileMapper.createIssuance(issuance));
    }

    /**
     * {@link CharacterFigureFileMapper#createIssuance(com.mesofi.collection.charactercatalog.model.Issuance)}
     */
    //@Test
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

        issuance = new Issuance();
        issuance.setPreorderDate(LocalDate.of(2023, 11, 12));
        issuance.setPreorderConfirmationDay(true);
        actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2023, 11, 12), actual.getPreorderDate());

        issuance = new Issuance();
        issuance.setReleaseDate(LocalDate.of(2023, 11, 12));
        issuance.setReleaseConfirmationDay(true);
        actual = characterFigureFileMapper.createIssuance(issuance);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2023, 11, 12), actual.getReleaseDate());
    }
}
