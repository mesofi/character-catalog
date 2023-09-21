/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {
    private CharacterFigureService characterFigureService;

    @Mock
    private CharacterRepository characterRepository;

    @BeforeEach
    public void before_each() {
        characterFigureService = new CharacterFigureService(characterRepository, null);
    }

    @AfterEach
    public void after_each() {
        characterFigureService = null;
    }

    @Test
    public void should_fail_character_creation_when_character_is_missing() {
        // service to be tested
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> characterFigureService.createNewCharacter(null));
        assertEquals("Unable to create a new character, provide a valid reference", exception.getMessage());
    }

    @Test
    public void should_fail_character_creation_when_base_name_is_missing() {
        CharacterFigure characterFigure = new CharacterFigure();
        // service to be tested
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> characterFigureService.createNewCharacter(characterFigure));
        assertEquals("Provide a base name for the character to be created", exception.getMessage());
    }

    @Test
    public void should_fail_character_creation_when_line_up_is_missing() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Poseidon");
        // service to be tested
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> characterFigureService.createNewCharacter(characterFigure));
        assertEquals("Provide a valid LineUp for the character", exception.getMessage());
    }

    @Test
    public void should_create_basic_character_successfully() {
        log.debug("Testing the creation of a new character ...");
        CharacterFigure characterFigureExpected = new CharacterFigure();
        characterFigureExpected.setId("650983fb0d94874526d643de");
        characterFigureExpected.setBaseName("Poseidon");
        characterFigureExpected.setLineUp(LineUp.MYTH_CLOTH);

        // When
        CharacterFigure basicCharacter = createBasicCharacter();
        when(characterRepository.save(basicCharacter)).thenReturn(characterFigureExpected);

        // service to be tested
        CharacterFigure characterFigureCreated = characterFigureService.createNewCharacter(basicCharacter);
        assertEquals("650983fb0d94874526d643de", characterFigureCreated.getId());
        assertEquals("Poseidon", characterFigureCreated.getBaseName());
        assertEquals(LineUp.MYTH_CLOTH, characterFigureCreated.getLineUp());

        // Verify
        verify(characterRepository).save(basicCharacter);
        verifyNoMoreInteractions(characterRepository);
    }

    private CharacterFigure createBasicCharacter() {
        CharacterFigure newCharacterFigure = new CharacterFigure();
        newCharacterFigure.setBaseName("Poseidon");
        newCharacterFigure.setLineUp(LineUp.MYTH_CLOTH);
        return newCharacterFigure;
    }

    @Test
    public void should_AssertWithError_WheNull() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V3);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Final Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setPlainCloth(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Plain Clothes)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Golden Genealogy Pegasus Seiya");
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Golden Genealogy Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V4);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V3);
        characterFigure.setOce(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V2);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Power of Gold~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V4);
        characterFigure.setOce(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth) ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Initial Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya & Goddess Athena");
        characterFigure.setOce(true);
        characterFigure.setBroken(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya & Goddess Athena ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V1);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Limited Gold~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~New Bronze Cloth~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setAnniversary(10);
        characterFigure.setGroup(Group.V4);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (God Cloth) ~10th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setOce(true);
        characterFigure.setGroup(Group.V2);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya ~Original Color Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setOce(true);
        characterFigure.setGroup(Group.V2);
        characterFigure.setAnniversary(40);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Original Color Edition) ~40th Anniversary Ver.~",
                characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setLineUp(LineUp.DDP);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V2);
        characterFigure.setGolden(true);
        characterFigure.setHk(true);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~ ~HK Version~",
                characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V5);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Heaven Chapter)", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V2);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V3);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setGolden(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Golden Limited Edition~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        characterFigure.setAnniversary(20);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~20th Anniversary Ver.~",
                characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setManga(true);
        characterFigure.setAnniversary(20);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Pegasus Seiya (Comic Version) ~20th Anniversary Ver.~", characterFigure.getDisplayedName());

        characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Aries Shion");
        characterFigure.setSurplice(true);
        characterFigureService.calculateFigureName(characterFigure);
        assertEquals("Aries Shion (Surplice)", characterFigure.getDisplayedName());
    }
}
