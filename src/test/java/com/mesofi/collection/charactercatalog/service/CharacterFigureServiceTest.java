/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mesofi.collection.charactercatalog.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFigureService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {

    @Mock
    private CharacterFigureRepository characterFigureRepository;
    @Mock
    private CharacterFigureModelMapper modelMapper;
    @Mock
    private CharacterFigureFileMapper fileMapper;

    private CharacterFigureService service;

    @BeforeEach
    public void init() {
        service = new CharacterFigureService(characterFigureRepository, modelMapper, fileMapper);
    }

    /**
     * Test for {@link CharacterFigureService#loadAllCharacters(MultipartFile)}
     */
    @Test
    public void should_fail_when_input_file_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.loadAllCharacters(null));
        assertEquals("The uploaded file is missing...", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#loadAllCharacters(MultipartFile)}
     */
    @Test
    public void should_load_all_records() throws IOException {

        doNothing().when(characterFigureRepository).deleteAll();
        when(fileMapper.fromLineToCharacterFigure(anyString())).thenReturn(new CharacterFigure());
        when(characterFigureRepository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        final String folder = "characters/";
        final String name = "MythCloth Catalog - CatalogMyth-min.tsv";
        final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));
        MultipartFile result = new MockMultipartFile(name, name, "text/plain", bytes);

        assertEquals(0, service.loadAllCharacters(result));
    }

    /**
     * Test for {@link CharacterFigureService#getEffectiveCharacters(List)}
     */
    @Test
    public void should_get_effective_characters_with_empty_records() {
        List<CharacterFigure> list = service.getEffectiveCharacters(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    /**
     * Test for {@link CharacterFigureService#getEffectiveCharacters(List)}
     */
    @Test
    public void should_get_effective_characters_with_restocks() {
        List<CharacterFigure> allCharacters = new ArrayList<>();
        allCharacters.add(createCharacter("Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false));
        allCharacters.add(createCharacter("Seiya", LocalDate.of(2019, 2, 1), LineUp.MYTH_CLOTH, Series.SAINT_SEIYA,
                Group.V2, true, true));
        allCharacters.add(createCharacter("Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false));

        List<CharacterFigure> list = service.getEffectiveCharacters(allCharacters);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Libra Dohko (God Cloth)", list.get(0).getOriginalName());
        assertNotNull(list.get(0).getRestocks());
        assertEquals(1, list.get(0).getRestocks().size());
        assertEquals("Seiya", list.get(1).getOriginalName());
        assertNull(list.get(1).getRestocks());
    }

    /**
     * Test for
     * {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)}
     */
    @Test
    public void should_get_figure_displayable_name() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        assertEquals("Pegasus Seiya ~Initial Bronze Cloth~", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V2);
        assertEquals("Pegasus Seiya", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        assertEquals("Pegasus Seiya ~New Bronze Cloth~", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V3);
        assertEquals("Pegasus Seiya ~Final Bronze Cloth~", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V4);
        assertEquals("Pegasus Seiya (God Cloth)", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V5);
        assertEquals("Pegasus Seiya (Heaven Chapter)", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.GOLD);
        assertEquals("Pegasus Seiya", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setPlainCloth(true);
        assertEquals("Pegasus Seiya (Plain Clothes)", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setPlainCloth(false);
        characterFigure.setBronzeToGold(true);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setGroup(Group.V1);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Limited Gold~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V2);
        assertEquals("Pegasus Seiya ~Power of Gold~", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        assertEquals("Pegasus Seiya ~Power of Gold~", service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V3);
        assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Golden Limited Edition~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setGroup(Group.V1);
        characterFigure.setManga(true);
        assertEquals("Pegasus Seiya ~Initial Bronze Cloth~ ~Comic Version~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setOce(true);
        assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Comic Version~ ~Original Color Edition~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setAnniversary(20);
        assertEquals(
                "Pegasus Seiya (Initial Bronze Cloth) (Comic Version) ~Original Color Edition~ ~20th Anniversary Ver.~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setHongKongVersion(true);
        assertEquals(
                "Pegasus Seiya (Initial Bronze Cloth) (Comic Version) ~Original Color Edition~ ~20th Anniversary Ver.~ ~HK Version~",
                service.calculateFigureDisplayableName(characterFigure));

        characterFigure.setSurplice(true);
        assertEquals(
                "Pegasus Seiya (Initial Bronze Cloth) (Comic Version) ~Original Color Edition~ ~20th Anniversary Ver.~ ~HK Version~ (Surplice)",
                service.calculateFigureDisplayableName(characterFigure));
    }

    /**
     * Test for {@link CharacterFigureService#retrieveAllCharacters()}
     */
    @Test
    public void should_get_all_characters() {
        List<CharacterFigureEntity> mockList = new ArrayList<>();
        CharacterFigureEntity entity1 = createCharacterFigure();
        CharacterFigure figure1 = createCharacter("Pegasus Seiya", LocalDate.of(2018, 1, 27), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false);

        mockList.add(entity1);
        when(characterFigureRepository.findAll(any(Sort.class))).thenReturn(mockList);
        when(modelMapper.toModel(entity1)).thenReturn(figure1);

        List<CharacterFigure> list = service.retrieveAllCharacters();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals("Pegasus Seiya", list.get(0).getDisplayableName());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_fail_when_new_character_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createNewCharacter(null));
        assertEquals("Provide a valid character", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_fail_when_new_character_base_name_is_missing() {
        CharacterFigure characterFigure = new CharacterFigure();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createNewCharacter(characterFigure));
        assertEquals("Provide a non empty base name", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_fail_when_new_character_group_is_missing() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createNewCharacter(characterFigure));
        assertEquals("Provide a valid group", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_create_new_character_with_default_values() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);

        CharacterFigureEntity entity = new CharacterFigureEntity();
        entity.setBaseName("Pegasus Seiya");
        entity.setGroup(Group.V1);
        when(modelMapper.toEntity(characterFigure)).thenReturn(entity);

        CharacterFigureEntity entity1 = new CharacterFigureEntity();
        entity1.setId("123");
        entity1.setBaseName("Pegasus Seiya");
        entity1.setGroup(Group.V1);
        when(characterFigureRepository.save(entity)).thenReturn(entity1);

        CharacterFigure characterFigureResult = new CharacterFigure();
        characterFigureResult.setId("123");
        characterFigureResult.setBaseName("Pegasus Seiya");
        characterFigureResult.setGroup(Group.V1);
        when(modelMapper.toModel(entity1)).thenReturn(characterFigureResult);

        CharacterFigure characterFigureExpected = service.createNewCharacter(characterFigure);
        assertNotNull(characterFigureExpected);
        assertEquals("123", characterFigureExpected.getId());
        assertEquals("Pegasus Seiya ~Initial Bronze Cloth~", characterFigureExpected.getDisplayableName());
        assertEquals(Group.V1, characterFigureExpected.getGroup());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_create_new_character() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Hypnos");
        characterFigure.setGroup(Group.V1);
        characterFigure.setOriginalName("Hypnos");
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setSeries(Series.SAINT_SEIYA);

        CharacterFigureEntity entity = new CharacterFigureEntity();
        entity.setBaseName("Hypnos");
        entity.setGroup(Group.V1);
        entity.setOriginalName("Hypnos");
        entity.setLineUp(LineUp.MYTH_CLOTH_EX);
        entity.setSeries(Series.SAINT_SEIYA);
        when(modelMapper.toEntity(characterFigure)).thenReturn(entity);

        CharacterFigureEntity entity1 = new CharacterFigureEntity();
        entity1.setId("123");
        entity1.setBaseName("Hypnos");
        entity1.setGroup(Group.V1);
        entity1.setOriginalName("Hypnos");
        entity1.setLineUp(LineUp.MYTH_CLOTH_EX);
        entity1.setSeries(Series.SAINT_SEIYA);
        when(characterFigureRepository.save(entity)).thenReturn(entity1);

        CharacterFigure characterFigureResult = new CharacterFigure();
        characterFigureResult.setId("123");
        characterFigureResult.setBaseName("Hypnos");
        characterFigureResult.setGroup(Group.V1);
        characterFigureResult.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigureResult.setSeries(Series.SAINT_SEIYA);
        when(modelMapper.toModel(entity1)).thenReturn(characterFigureResult);

        CharacterFigure characterFigureExpected = service.createNewCharacter(characterFigure);
        assertNotNull(characterFigureExpected);
        assertEquals("123", characterFigureExpected.getId());
        assertEquals("Hypnos ~Initial Bronze Cloth~", characterFigureExpected.getDisplayableName());
        assertEquals(Group.V1, characterFigureExpected.getGroup());
        assertEquals(LineUp.MYTH_CLOTH_EX, characterFigureExpected.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigureExpected.getSeries());
    }

    private CharacterFigureEntity createCharacterFigure() {
        CharacterFigureEntity characterFigure = new CharacterFigureEntity();
        characterFigure.setOriginalName("Pegasus Seiya");
        characterFigure.setBaseName("Pegasus Seiya");
        return characterFigure;
    }

    private CharacterFigure createCharacter(String originalName, LocalDate releaseDate, LineUp lineUp, Series series,
            Group group, boolean oce, boolean revival) {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setOriginalName(originalName);
        characterFigure.setBaseName(originalName);
        Issuance issuanceJPY = new Issuance();
        issuanceJPY.setReleaseConfirmationDay(true);
        issuanceJPY.setReleaseDate(releaseDate);
        issuanceJPY.setBasePrice(new BigDecimal("12000"));
        characterFigure.setIssuanceJPY(issuanceJPY);
        characterFigure.setLineUp(lineUp);
        characterFigure.setSeries(series);
        characterFigure.setGroup(group);
        characterFigure.setOce(oce);
        characterFigure.setRevival(revival);

        return characterFigure;
    }
}
