/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for {@link CharacterFigure}
 * 
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFigureTest {

    private CharacterFigure characterFigure;

    @Mock
    private CharacterFigure characterFigureMock;

    @BeforeEach
    public void beforeEach() {
        characterFigure = new CharacterFigure();
    }

    @ParameterizedTest
    @MethodSource("provideBooleanValues")
    public void should_verify_getter_setter_properties(boolean metalBody, boolean oce, boolean revival,
            boolean plainCloth, boolean brokenCloth, boolean bronzeToGold, boolean gold, boolean hongKongVersion,
            boolean manga, boolean surplice, boolean set) {
        characterFigure.setId("3232kl3232lk");
        characterFigure.setOriginalName("Pegasus Seiya");
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setDisplayableName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setSeries(Series.SAINT_SEIYA);
        characterFigure.setGroup(Group.V1);
        characterFigure.setMetalBody(metalBody);
        characterFigure.setOce(oce);
        characterFigure.setRevival(revival);
        characterFigure.setPlainCloth(plainCloth);
        characterFigure.setBrokenCloth(brokenCloth);
        characterFigure.setBronzeToGold(bronzeToGold);
        characterFigure.setGold(gold);
        characterFigure.setHongKongVersion(hongKongVersion);
        characterFigure.setManga(manga);
        characterFigure.setSurplice(surplice);
        characterFigure.setSet(set);
        characterFigure.setAnniversary(20);
        characterFigure.setTags(Set.of("Saint"));
        characterFigure.setImages(List.of(new GalleryImage()));

        assertEquals("3232kl3232lk", characterFigure.getId());
        assertEquals("Pegasus Seiya", characterFigure.getOriginalName());
        assertEquals("Pegasus Seiya", characterFigure.getBaseName());
        assertEquals("Pegasus Seiya", characterFigure.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH, characterFigure.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigure.getSeries());
        assertEquals(Group.V1, characterFigure.getGroup());
        assertEquals(metalBody, characterFigure.isMetalBody());
        assertEquals(oce, characterFigure.isOce());
        assertEquals(revival, characterFigure.isRevival());
        assertEquals(plainCloth, characterFigure.isPlainCloth());
        assertEquals(brokenCloth, characterFigure.isBrokenCloth());
        assertEquals(bronzeToGold, characterFigure.isBronzeToGold());
        assertEquals(gold, characterFigure.isGold());
        assertEquals(hongKongVersion, characterFigure.isHongKongVersion());
        assertEquals(manga, characterFigure.isManga());
        assertEquals(surplice, characterFigure.isSurplice());
        assertEquals(set, characterFigure.isSet());
        assertEquals(20, characterFigure.getAnniversary());
        assertEquals(Set.of("Saint"), characterFigure.getTags());
        assertEquals(List.of(new GalleryImage()), characterFigure.getImages());

    }

    @Test
    public void should_verify_equality_1() {
        CharacterFigure otherCharacterFigure = characterFigure;
        assertEquals(characterFigure, otherCharacterFigure);
    }

    @Test
    public void should_verify_equality_2() {
        assertNotEquals("", characterFigure);
    }

    @Test
    public void should_verify_equality_3() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(false);
        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_4() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(false);
        characterFigure.setMetalBody(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_5() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(false);

        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_6() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_7() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_8() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_9() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_10() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_11() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_12() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(false);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_13() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_14() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(20);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(20);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_15() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(15);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(20);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_16() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn("Seiya");
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName("Seiya");

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_17() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn("Seiya");
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName("Shiryu");

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_18() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_19() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.CROWN);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_20() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(null);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_21() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_22() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(Series.SAINT_SEIYA);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setSeries(Series.SAINT_SEIYA);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_23() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(Series.OMEGA);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setSeries(Series.SAINT_SEIYA);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_24() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(null);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setSeries(null);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_25() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(null);
        when(characterFigureMock.getGroup()).thenReturn(null);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.BLACK);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_26() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(null);
        when(characterFigureMock.getGroup()).thenReturn(Group.BLACK);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(null);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_27() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(null);
        when(characterFigureMock.getGroup()).thenReturn(Group.GOD);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.BLACK);

        assertNotEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_equality_28() {
        when(characterFigureMock.canEqual(characterFigure)).thenReturn(true);

        when(characterFigureMock.isMetalBody()).thenReturn(true);
        when(characterFigureMock.isOce()).thenReturn(true);
        when(characterFigureMock.isRevival()).thenReturn(true);
        when(characterFigureMock.isPlainCloth()).thenReturn(true);
        when(characterFigureMock.isBronzeToGold()).thenReturn(true);
        when(characterFigureMock.isGold()).thenReturn(true);
        when(characterFigureMock.isHongKongVersion()).thenReturn(true);
        when(characterFigureMock.isManga()).thenReturn(true);
        when(characterFigureMock.isSurplice()).thenReturn(true);
        when(characterFigureMock.getAnniversary()).thenReturn(null);
        when(characterFigureMock.getBaseName()).thenReturn(null);
        when(characterFigureMock.getLineUp()).thenReturn(LineUp.MYTH_CLOTH);
        when(characterFigureMock.getSeries()).thenReturn(null);
        when(characterFigureMock.getGroup()).thenReturn(Group.GOD);
        
        characterFigure.setMetalBody(true);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setAnniversary(null);
        characterFigure.setBaseName(null);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.GOD);

        assertEquals(characterFigure, characterFigureMock);
    }

    @Test
    public void should_verify_same_type() {
        CharacterFigure otherCharacterFigure = new CharacterFigure();
        assertTrue(characterFigure.canEqual(otherCharacterFigure));
        assertFalse(characterFigure.canEqual("Other"));
    }

    @Test
    public void should_get_hash_code() {
        assertEquals(-1369399865, characterFigure.hashCode());
    }

    private static Stream<Arguments> provideBooleanValues() {
        // @formatter:off
        return Stream.of(
                Arguments.of(true, true, true, true, true, true, true, true, true, true, true),
                Arguments.of(false, false, false, false, false, false, false, false, false, false, false)
        );
        // @formatter:on
    }
}
