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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFigureService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CharacterFigureServiceTest {

    @Mock
    private CharacterFigureRepository repository;
    @Mock
    private CharacterFigureModelMapper modelMapper;
    @Mock
    private CharacterFigureFileMapper fileMapper;

    private CharacterFigureService service;

    @BeforeEach
    public void init() {
        service = new CharacterFigureService(repository, modelMapper, fileMapper);
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

        doNothing().when(repository).deleteAll();
        when(fileMapper.fromLineToCharacterFigure(anyString())).thenReturn(new CharacterFigure());
        when(repository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

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
        allCharacters.add(createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false));
        allCharacters.add(createFigure(null, "Seiya", LocalDate.of(2019, 2, 1), LineUp.MYTH_CLOTH, Series.SAINT_SEIYA,
                Group.V2, true, true));
        allCharacters.add(createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18), LineUp.MYTH_CLOTH_EX,
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
        CharacterFigureEntity entity1 = createFigureEntity("1", "Pegasus Seiya", "Pegasus Seiya", Group.V1, false);
        CharacterFigure figure1 = createFigure(null, "Pegasus Seiya", LocalDate.of(2018, 1, 27), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false);

        mockList.add(entity1);
        when(repository.findAll(any(Sort.class))).thenReturn(mockList);
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
        Issuance issuanceJPY = new Issuance();
        issuanceJPY.setReleaseDate(LocalDate.of(2023, 2, 3));
        characterFigure.setIssuanceJPY(issuanceJPY);

        CharacterFigureEntity entity = new CharacterFigureEntity();
        entity.setBaseName("Pegasus Seiya");
        entity.setGroup(Group.V1);
        when(modelMapper.toEntity(characterFigure)).thenReturn(entity);

        CharacterFigureEntity entity1 = new CharacterFigureEntity();
        entity1.setId("123");
        entity1.setBaseName("Pegasus Seiya");
        entity1.setGroup(Group.V1);
        when(repository.save(entity)).thenReturn(entity1);

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
        when(repository.save(entity)).thenReturn(entity1);

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

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_create_new_character_with_restock() {
        CharacterFigure newCharacterFigure = new CharacterFigure();
        newCharacterFigure.setBaseName("Virgo Shaka");
        newCharacterFigure.setOriginalName("Virgo Shaka");
        newCharacterFigure.setGroup(Group.GOLD);
        newCharacterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        newCharacterFigure.setSeries(Series.SAINT_SEIYA);
        newCharacterFigure.setRevival(true);

        CharacterFigureEntity e1 = createFigureEntity("1", "Alraune Queen", "Alraune Queen", Group.SILVER, false);
        CharacterFigureEntity e2 = createFigureEntity("2", "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        CharacterFigureEntity e3 = createFigureEntity("3", "Leo Aiolia", "Leo Aiolia", Group.GOLD, true);
        CharacterFigureEntity e4 = createFigureEntity("4", "Sagittarius Seiya", "Sagittarius Seiya", Group.GOLD, false);
        List<CharacterFigureEntity> list = List.of(e1, e2, e3, e4);

        when(repository.findAll(any(Sort.class))).thenReturn(list);

        when(modelMapper.toModel(e1)).thenReturn(createFigure("1", "Alraune Queen", LocalDate.now(),
                LineUp.MYTH_CLOTH_EX, Series.SAINT_SEIYA, Group.SILVER, false, false));
        when(modelMapper.toModel(e2)).thenReturn(createFigure("2", "Virgo Shaka", LocalDate.now(), LineUp.MYTH_CLOTH_EX,
                Series.SAINT_SEIYA, Group.GOLD, false, true));
        when(modelMapper.toModel(e3)).thenReturn(createFigure("3", "Leo Aiolia", LocalDate.now(), LineUp.MYTH_CLOTH_EX,
                Series.SAINT_SEIYA, Group.GOLD, false, true));
        when(modelMapper.toModel(e4)).thenReturn(createFigure("4", "Sagittarius Seiya", LocalDate.now(),
                LineUp.MYTH_CLOTH_EX, Series.SAINT_SEIYA, Group.GOLD, false, false));

        CharacterFigure characterFigureExpected = service.createNewCharacter(newCharacterFigure);
        assertNotNull(characterFigureExpected);
        assertEquals("2", characterFigureExpected.getId());
        assertNull(characterFigureExpected.getOriginalName());
        assertNull(characterFigureExpected.getBaseName());
        assertEquals("Virgo Shaka", characterFigureExpected.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, characterFigureExpected.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigureExpected.getSeries());
        assertEquals(Group.GOLD, characterFigureExpected.getGroup());
        assertFalse(characterFigureExpected.isMetalBody());
        assertFalse(characterFigureExpected.isOce());
        assertTrue(characterFigureExpected.isRevival());
        assertFalse(characterFigureExpected.isPlainCloth());
        assertFalse(characterFigureExpected.isBrokenCloth());
        assertFalse(characterFigureExpected.isBronzeToGold());
        assertFalse(characterFigureExpected.isGold());
        assertFalse(characterFigureExpected.isHongKongVersion());
        assertFalse(characterFigureExpected.isManga());
        assertFalse(characterFigureExpected.isSurplice());
        assertFalse(characterFigureExpected.isSet());
        assertNull(characterFigureExpected.getAnniversary());
        assertNotNull(characterFigureExpected.getRestocks());
        assertEquals(1, characterFigureExpected.getRestocks().size());
        assertNull(characterFigureExpected.getRestocks().get(0).getIssuanceJPY());
        assertNull(characterFigureExpected.getRestocks().get(0).getIssuanceMXN());
        assertFalse(characterFigureExpected.getRestocks().get(0).isFutureRelease());
        assertNull(characterFigureExpected.getRestocks().get(0).getUrl());
        assertNull(characterFigureExpected.getRestocks().get(0).getDistribution());
        assertNull(characterFigureExpected.getRestocks().get(0).getRemarks());
        assertNotNull(characterFigureExpected.getIssuanceJPY());
        assertEquals(new BigDecimal(12000), characterFigureExpected.getIssuanceJPY().getBasePrice());
        assertEquals(new BigDecimal("13200.00"), characterFigureExpected.getIssuanceJPY().getReleasePrice());
        assertNull(characterFigureExpected.getIssuanceJPY().getFirstAnnouncementDate());
        assertNull(characterFigureExpected.getIssuanceJPY().getPreorderDate());
        assertNull(characterFigureExpected.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.of(2023, 9, 28), characterFigureExpected.getIssuanceJPY().getReleaseDate());
        assertTrue(characterFigureExpected.getIssuanceJPY().getReleaseConfirmationDay());
        assertNull(characterFigureExpected.getIssuanceMXN());
        assertFalse(characterFigureExpected.isFutureRelease());
        assertNull(characterFigureExpected.getUrl());
        assertNull(characterFigureExpected.getDistribution());
        assertNull(characterFigureExpected.getRemarks());
    }

    /**
     * Test for {@link CharacterFigureService#createNewCharacter(CharacterFigure)}
     */
    @Test
    public void should_create_new_character_with_multiple_restocks() {
        CharacterFigure newCharacter = new CharacterFigure();
        newCharacter.setBaseName("Virgo Shaka");
        newCharacter.setGroup(Group.GOLD);
        newCharacter.setRevival(true);

        // no records exist in the DB yet
        when(repository.findAll(any(Sort.class))).thenReturn(new ArrayList<>());

        CharacterFigureEntity entity = createFigureEntity(null, "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(modelMapper.toEntity(any(CharacterFigure.class))).thenReturn(entity);

        CharacterFigureEntity savedEntity = createFigureEntity("1", "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(repository.save(any(CharacterFigureEntity.class))).thenReturn(savedEntity);

        CharacterFigure savedCharacter = new CharacterFigure();
        savedCharacter.setId("1");
        savedCharacter.setBaseName("Virgo Shaka");
        savedCharacter.setGroup(Group.GOLD);
        savedCharacter.setRevival(true);
        savedCharacter.setLineUp(LineUp.MYTH_CLOTH_EX);
        savedCharacter.setSeries(Series.SAINT_SEIYA);
        when(modelMapper.toModel(any(CharacterFigureEntity.class))).thenReturn(savedCharacter);

        // calls the creation of the character for the first time.
        CharacterFigure characterFigureExpected = service.createNewCharacter(newCharacter);
        assertNotNull(characterFigureExpected);
        assertEquals("1", characterFigureExpected.getId());
        assertNull(characterFigureExpected.getOriginalName());
        assertNull(characterFigureExpected.getBaseName());
        assertEquals("Virgo Shaka", characterFigureExpected.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, characterFigureExpected.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigureExpected.getSeries());
        assertEquals(Group.GOLD, characterFigureExpected.getGroup());
        assertFalse(characterFigureExpected.isMetalBody());
        assertFalse(characterFigureExpected.isOce());
        assertTrue(characterFigureExpected.isRevival());
        assertFalse(characterFigureExpected.isPlainCloth());
        assertFalse(characterFigureExpected.isBrokenCloth());
        assertFalse(characterFigureExpected.isBronzeToGold());
        assertFalse(characterFigureExpected.isGold());
        assertFalse(characterFigureExpected.isHongKongVersion());
        assertFalse(characterFigureExpected.isManga());
        assertFalse(characterFigureExpected.isSurplice());
        assertFalse(characterFigureExpected.isSet());
        assertNull(characterFigureExpected.getAnniversary());
        assertNull(characterFigureExpected.getRestocks());
        assertNull(characterFigureExpected.getIssuanceJPY());
        assertNull(characterFigureExpected.getIssuanceMXN());
        assertFalse(characterFigureExpected.isFutureRelease());
        assertNull(characterFigureExpected.getUrl());
        assertNull(characterFigureExpected.getDistribution());
        assertNull(characterFigureExpected.getRemarks());

        // We found an existing record.
        CharacterFigureEntity existingEntity = createFigureEntity("1", "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(existingEntity));

        CharacterFigure mappedCharacter = new CharacterFigure();
        mappedCharacter.setId("1");
        mappedCharacter.setOriginalName("Virgo Shaka");
        mappedCharacter.setBaseName("Virgo Shaka");
        mappedCharacter.setGroup(Group.GOLD);
        mappedCharacter.setRevival(true);
        mappedCharacter.setLineUp(LineUp.MYTH_CLOTH_EX);
        mappedCharacter.setSeries(Series.SAINT_SEIYA);
        when(modelMapper.toModel(existingEntity)).thenReturn(mappedCharacter);

        Optional<CharacterFigureEntity> entityFound = Optional
                .of(createFigureEntity("1", "Virgo Shaka", "Virgo Shaka", Group.GOLD, true));
        when(repository.findById("1")).thenReturn(entityFound);

        // calls the creation of the character for the second time.
        CharacterFigure characterFigureWithRestock = service.createNewCharacter(newCharacter);
        assertNotNull(characterFigureWithRestock);
        assertEquals("1", characterFigureWithRestock.getId());
        assertNull(characterFigureWithRestock.getOriginalName());
        assertNull(characterFigureWithRestock.getBaseName());
        assertEquals("Virgo Shaka", characterFigureWithRestock.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, characterFigureWithRestock.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigureWithRestock.getSeries());
        assertEquals(Group.GOLD, characterFigureWithRestock.getGroup());
        assertFalse(characterFigureWithRestock.isMetalBody());
        assertFalse(characterFigureWithRestock.isOce());
        assertTrue(characterFigureWithRestock.isRevival());
        assertFalse(characterFigureWithRestock.isPlainCloth());
        assertFalse(characterFigureWithRestock.isBrokenCloth());
        assertFalse(characterFigureWithRestock.isBronzeToGold());
        assertFalse(characterFigureWithRestock.isGold());
        assertFalse(characterFigureWithRestock.isHongKongVersion());
        assertFalse(characterFigureWithRestock.isManga());
        assertFalse(characterFigureWithRestock.isSurplice());
        assertFalse(characterFigureWithRestock.isSet());
        assertNull(characterFigureWithRestock.getAnniversary());
        assertNotNull(characterFigureWithRestock.getRestocks());
        assertEquals(1, characterFigureWithRestock.getRestocks().size());
        assertFalse(characterFigureWithRestock.getRestocks().get(0).isFutureRelease());
        assertNull(characterFigureWithRestock.getRestocks().get(0).getUrl());
        assertNull(characterFigureWithRestock.getRestocks().get(0).getRemarks());
        assertNull(characterFigureWithRestock.getRestocks().get(0).getDistribution());
        assertNull(characterFigureWithRestock.getRestocks().get(0).getIssuanceMXN());
        assertNull(characterFigureWithRestock.getRestocks().get(0).getIssuanceJPY());
        assertNull(characterFigureWithRestock.getIssuanceJPY());
        assertNull(characterFigureWithRestock.getIssuanceMXN());
        assertFalse(characterFigureWithRestock.isFutureRelease());
        assertNull(characterFigureWithRestock.getUrl());
        assertNull(characterFigureWithRestock.getDistribution());
        assertNull(characterFigureWithRestock.getRemarks());
    }

    private CharacterFigureEntity createFigureEntity(String id, String originalName, String baseName, Group group,
            boolean revival) {
        CharacterFigureEntity characterFigure = new CharacterFigureEntity();
        characterFigure.setId(id);
        characterFigure.setOriginalName(originalName);
        characterFigure.setBaseName(baseName);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setSeries(Series.SAINT_SEIYA);
        characterFigure.setGroup(group);
        characterFigure.setMetal(false);
        characterFigure.setOce(false);
        characterFigure.setRevival(revival);
        characterFigure.setPlainCloth(false);
        characterFigure.setHk(false);
        characterFigure.setManga(false);
        characterFigure.setSurplice(false);

        return characterFigure;
    }

    private CharacterFigure createFigure(String id, String originalName, LocalDate releaseDate, LineUp lineUp,
            Series series, Group group, boolean oce, boolean revival) {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setId(id);
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
