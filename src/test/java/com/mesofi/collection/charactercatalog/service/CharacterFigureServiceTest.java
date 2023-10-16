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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureNotFoundException;
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
        CharacterFigure cf = new CharacterFigure();
        cf.setBaseName("MyName");
        when(fileMapper.fromLineToCharacterFigure(anyString())).thenReturn(cf);
        when(repository.saveAll(anyIterable())).thenReturn(new ArrayList<>());

        final String folder = "characters/";
        final String name = "MythCloth Catalog - CatalogMyth-min.tsv";
        final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));
        MultipartFile result = new MockMultipartFile(name, name, MediaType.TEXT_PLAIN_VALUE, bytes);

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
     * Test for {@link CharacterFigureService#getEffectiveCharacters(List)}
     */
    @Test
    public void should_get_effective_characters_with_restocks_and_new_tags() {
        List<CharacterFigure> allCharacters = new ArrayList<>();
        CharacterFigure c1 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);

        CharacterFigure c2 = createFigure(null, "Seiya", LocalDate.of(2019, 2, 1), LineUp.MYTH_CLOTH,
                Series.SAINT_SEIYA, Group.V2, true, true);

        CharacterFigure c3 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);
        c3.setTags(Set.of("god", "gold", "ex")); // adds new tags

        allCharacters.add(c1);
        allCharacters.add(c2);
        allCharacters.add(c3);

        List<CharacterFigure> list = service.getEffectiveCharacters(allCharacters);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Libra Dohko (God Cloth)", list.get(0).getOriginalName());
        assertNotNull(list.get(0).getRestocks());
        assertNotNull(list.get(0).getTags());
        assertEquals(3, list.get(0).getTags().size());
        assertEquals(Set.of("god", "gold", "ex"), list.get(0).getTags());

        assertEquals(1, list.get(0).getRestocks().size());
        assertEquals("Seiya", list.get(1).getOriginalName());
        assertNull(list.get(1).getRestocks());
    }

    /**
     * Test for {@link CharacterFigureService#getEffectiveCharacters(List)}
     */
    @Test
    public void should_get_effective_characters_with_restocks_and_existing_tags() {
        List<CharacterFigure> allCharacters = new ArrayList<>();
        CharacterFigure c1 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);
        c1.setTags(Set.of("god", "gold", "ex")); // adds new tags

        CharacterFigure c2 = createFigure(null, "Seiya", LocalDate.of(2019, 2, 1), LineUp.MYTH_CLOTH,
                Series.SAINT_SEIYA, Group.V2, true, true);

        CharacterFigure c3 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);

        allCharacters.add(c1);
        allCharacters.add(c2);
        allCharacters.add(c3);

        List<CharacterFigure> list = service.getEffectiveCharacters(allCharacters);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Libra Dohko (God Cloth)", list.get(0).getOriginalName());
        assertNotNull(list.get(0).getRestocks());
        assertNotNull(list.get(0).getTags());
        assertEquals(3, list.get(0).getTags().size());
        assertEquals(Set.of("god", "gold", "ex"), list.get(0).getTags());

        assertEquals(1, list.get(0).getRestocks().size());
        assertEquals("Seiya", list.get(1).getOriginalName());
        assertNull(list.get(1).getRestocks());
    }

    /**
     * Test for {@link CharacterFigureService#getEffectiveCharacters(List)}
     */
    @Test
    public void should_get_effective_characters_with_restocks_and_new_existing_tags() {
        List<CharacterFigure> allCharacters = new ArrayList<>();
        CharacterFigure c1 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);
        c1.setTags(Set.of("god", "gold", "ex")); // adds new tags

        CharacterFigure c2 = createFigure(null, "Seiya", LocalDate.of(2019, 2, 1), LineUp.MYTH_CLOTH,
                Series.SAINT_SEIYA, Group.V2, true, true);

        CharacterFigure c3 = createFigure(null, "Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18),
                LineUp.MYTH_CLOTH_EX, Series.SOG, Group.GOLD, false, false);
        c3.setTags(Set.of("god", "gold", "libra", "ex", "revival")); // adds new tags

        allCharacters.add(c1);
        allCharacters.add(c2);
        allCharacters.add(c3);

        List<CharacterFigure> list = service.getEffectiveCharacters(allCharacters);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("Libra Dohko (God Cloth)", list.get(0).getOriginalName());
        assertNotNull(list.get(0).getRestocks());
        assertNotNull(list.get(0).getTags());
        assertEquals(5, list.get(0).getTags().size());
        assertEquals(Set.of("gold", "ex", "libra", "revival", "god"), list.get(0).getTags());

        assertEquals(1, list.get(0).getRestocks().size());
        assertEquals("Seiya", list.get(1).getOriginalName());
        assertNull(list.get(1).getRestocks());
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
     * Test for {@link CharacterFigureService#retrieveCharactersById(String)}
     */
    @Test
    public void should_fail_when_id_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.retrieveCharactersById(null));
        assertEquals("Provide a non empty id to find a character", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#retrieveCharactersById(String)}
     */
    @Test
    public void should_fail_when_id_does_not_exist() {
        final String id = "unknown";

        when(repository.findById(id)).thenReturn(Optional.empty());

        CharacterFigureNotFoundException exception = assertThrows(CharacterFigureNotFoundException.class,
                () -> service.retrieveCharactersById(id));
        assertEquals("Character not found with id: unknown", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#retrieveCharactersById(String)}
     */
    @Test
    public void should_retrieve_character_by_identifier() {
        final String id = "65161253b30e8c1a61696f2c";

        CharacterFigureEntity e1 = createFigureEntity("1", "Alraune Queen", "Alraune Queen", Group.SILVER, false);
        Optional<CharacterFigureEntity> found = Optional.of(e1);
        when(repository.findById(id)).thenReturn(found);

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setId(e1.getId());
        characterFigure.setOriginalName(e1.getOriginalName());
        characterFigure.setBaseName(e1.getBaseName());
        characterFigure.setGroup(e1.getGroup());
        characterFigure.setLineUp(e1.getLineUp());
        characterFigure.setSeries(e1.getSeries());

        when(modelMapper.toModel(e1)).thenReturn(characterFigure);

        CharacterFigure characterFigureExpected = service.retrieveCharactersById(id);
        assertNotNull(characterFigureExpected);
        assertEquals("1", characterFigureExpected.getId());
        assertNull(characterFigureExpected.getOriginalName());
        assertNull(characterFigureExpected.getBaseName());
        assertEquals("Alraune Queen", characterFigureExpected.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, characterFigureExpected.getLineUp());
        assertEquals(Series.SAINT_SEIYA, characterFigureExpected.getSeries());
        assertEquals(Group.SILVER, characterFigureExpected.getGroup());
        assertFalse(characterFigureExpected.isMetalBody());
        assertFalse(characterFigureExpected.isOce());
        assertFalse(characterFigureExpected.isRevival());
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
    public void should_fail_when_new_character_contains_restocks() {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setBaseName("Pegasus Seiya");
        characterFigure.setGroup(Group.V1);
        characterFigure.setRestocks(new ArrayList<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.createNewCharacter(characterFigure));
        assertEquals("Remove restock reference", exception.getMessage());
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
        assertTrue(characterFigureExpected.getRestocks().get(0).isFutureRelease());
        assertNull(characterFigureExpected.getRestocks().get(0).getUrl());
        assertNull(characterFigureExpected.getRestocks().get(0).getDistribution());
        assertNull(characterFigureExpected.getRestocks().get(0).getRemarks());
        assertNotNull(characterFigureExpected.getIssuanceJPY());
        assertEquals(new BigDecimal(12000), characterFigureExpected.getIssuanceJPY().getBasePrice());
        assertEquals(new BigDecimal("13200.00"), characterFigureExpected.getIssuanceJPY().getReleasePrice());
        assertNull(characterFigureExpected.getIssuanceJPY().getFirstAnnouncementDate());
        assertNull(characterFigureExpected.getIssuanceJPY().getPreorderDate());
        assertNull(characterFigureExpected.getIssuanceJPY().getPreorderConfirmationDay());
        assertEquals(LocalDate.now(), characterFigureExpected.getIssuanceJPY().getReleaseDate());
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
        assertTrue(characterFigureWithRestock.getRestocks().get(0).isFutureRelease());
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

    /**
     * Test for
     * {@link CharacterFigureService#updateExistingCharacter(String, CharacterFigure)}
     */
    @Test
    public void should_fail_update_character_when_id_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateExistingCharacter(null, null));
        assertEquals("Provide a non empty character id", exception.getMessage());
    }

    /**
     * Test for
     * {@link CharacterFigureService#updateExistingCharacter(String, CharacterFigure)}
     */
    @Test
    public void should_fail_update_character_when_updated_character_is_missing() {
        String id = "122kk3j4h5hdn";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateExistingCharacter(id, null));
        assertEquals("Provide a valid character", exception.getMessage());
    }

    /**
     * Test for
     * {@link CharacterFigureService#updateExistingCharacter(String, CharacterFigure)}
     */
    @Test
    public void should_fail_update_character_when_id_is_not_found() {
        String id = "122kk3j4h5hdn";
        CharacterFigure updatedCharacter = new CharacterFigure();
        updatedCharacter.setBaseName("Scorpio Milo");
        updatedCharacter.setGroup(Group.GOLD);

        CharacterFigureNotFoundException exception = assertThrows(CharacterFigureNotFoundException.class,
                () -> service.updateExistingCharacter(id, updatedCharacter));
        assertEquals("Character not found with id: 122kk3j4h5hdn", exception.getMessage());
    }

    /**
     * Test for
     * {@link CharacterFigureService#updateExistingCharacter(String, CharacterFigure)}
     */
    @Test
    public void should_update_character_successfully() {
        String id = "122kk3j4h5hdn";
        CharacterFigure updatedCharacter = new CharacterFigure();
        updatedCharacter.setOriginalName("Scorpio Ecarlet");
        updatedCharacter.setBaseName("Scorpio Ecarlet");
        updatedCharacter.setGroup(Group.GOLD);
        updatedCharacter.setRevival(true);

        CharacterFigureEntity updatedCharacterEntity = createFigureEntity(null, "Scorpio Ecarlet", "Scorpio Ecarlet",
                Group.GOLD, true);
        when(modelMapper.toEntity(updatedCharacter)).thenReturn(updatedCharacterEntity);

        CharacterFigureEntity entity = createFigureEntity(id, "Scorpio Milo", "Scorpio Milo", Group.GOLD, false);
        entity.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CharacterFigure figure = new CharacterFigure();
        figure.setId(id);
        figure.setOriginalName("Scorpio Ecarlet");
        figure.setBaseName("Scorpio Ecarlet");
        figure.setGroup(Group.GOLD);
        figure.setRevival(true);
        figure.setLineUp(LineUp.MYTH_CLOTH_EX);
        figure.setSeries(Series.SAINT_SEIYA);
        when(modelMapper.toModel(any())).thenReturn(figure);

        CharacterFigure actual = service.updateExistingCharacter(id, updatedCharacter);
        assertNotNull(actual);
        assertNotNull(actual);
        assertEquals("122kk3j4h5hdn", actual.getId());
        assertNull(actual.getOriginalName());
        assertNull(actual.getBaseName());
        assertEquals("Scorpio Ecarlet", actual.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, actual.getLineUp());
        assertEquals(Series.SAINT_SEIYA, actual.getSeries());
        assertEquals(Group.GOLD, actual.getGroup());
        assertFalse(actual.isMetalBody());
        assertFalse(actual.isOce());
        assertTrue(actual.isRevival());
        assertFalse(actual.isPlainCloth());
        assertFalse(actual.isBrokenCloth());
        assertFalse(actual.isBronzeToGold());
        assertFalse(actual.isGold());
        assertFalse(actual.isHongKongVersion());
        assertFalse(actual.isManga());
        assertFalse(actual.isSurplice());
        assertFalse(actual.isSet());
        assertNull(actual.getAnniversary());
        assertNull(actual.getRestocks());
        assertNull(actual.getIssuanceJPY());
        assertNull(actual.getIssuanceMXN());
        assertFalse(actual.isFutureRelease());
        assertNull(actual.getUrl());
        assertNull(actual.getDistribution());
        assertNull(actual.getRemarks());
    }

    /**
     * Test for {@link CharacterFigureService#updateTagsInCharacter(String, Set)}
     */
    @Test
    public void should_fail_character_update_tags_when_id_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateTagsInCharacter(null, null));
        assertEquals("Provide a non empty character id", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#updateTagsInCharacter(String, Set)}
     */
    @Test
    public void should_fail_character_update_tags_when_id_not_found() {
        final String id = "223"; // not found
        CharacterFigureNotFoundException exception = assertThrows(CharacterFigureNotFoundException.class,
                () -> service.updateTagsInCharacter(id, null));
        assertEquals("Character not found with id: 223", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#updateTagsInCharacter(String, Set)}
     */
    @Test
    public void should_avoid_update_character_tags_when_no_tags_available() {
        final String id = "65215079a5d1a04590202d6f";

        CharacterFigureEntity entity = createFigureEntity("1", "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setId(id);
        characterFigure.setBaseName("Virgo Shaka");
        characterFigure.setGroup(Group.GOLD);
        characterFigure.setRevival(true);
        characterFigure.setSeries(Series.SAINT_SEIYA);
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

        when(modelMapper.toModel(any())).thenReturn(characterFigure);

        // the tags do not exist
        CharacterFigure actual = service.updateTagsInCharacter(id, null);
        assertNotNull(actual);
        assertEquals("65215079a5d1a04590202d6f", actual.getId());
        assertNull(actual.getOriginalName());
        assertEquals("Virgo Shaka", actual.getBaseName());
        assertNull(actual.getDisplayableName());
        assertEquals(LineUp.MYTH_CLOTH_EX, actual.getLineUp());
        assertEquals(Series.SAINT_SEIYA, actual.getSeries());
        assertEquals(Group.GOLD, actual.getGroup());
        assertFalse(actual.isMetalBody());
        assertFalse(actual.isOce());
        assertTrue(actual.isRevival());
        assertFalse(actual.isPlainCloth());
        assertFalse(actual.isBrokenCloth());
        assertFalse(actual.isBronzeToGold());
        assertFalse(actual.isGold());
        assertFalse(actual.isHongKongVersion());
        assertFalse(actual.isManga());
        assertFalse(actual.isSurplice());
        assertFalse(actual.isSet());
        assertNull(actual.getAnniversary());
        assertNull(actual.getRestocks());
        assertNull(actual.getIssuanceJPY());
        assertNull(actual.getIssuanceMXN());
        assertFalse(actual.isFutureRelease());
        assertNull(actual.getUrl());
        assertNull(actual.getDistribution());
        assertNull(actual.getRemarks());
        assertNull(actual.getTags());
    }

    /**
     * Test for {@link CharacterFigureService#updateTagsInCharacter(String, Set)}
     */
    @Test
    public void should_update_tags_when_existing_tags_are_empty_and_new_tags_are_provided() {
        final String id = "65215079a5d1a04590202d6f";

        CharacterFigureEntity entity = createFigureEntity(id, "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            CharacterFigureEntity e = (CharacterFigureEntity) args[0];
            assertEquals(id, e.getId());
            assertEquals("Virgo Shaka", e.getBaseName());
            assertEquals("Virgo Shaka", e.getOriginalName());
            assertEquals(Group.GOLD, e.getGroup());
            assertTrue(e.isRevival());

            return null;
        }).when(repository).save(any(CharacterFigureEntity.class));

        CharacterFigureEntity e = new CharacterFigureEntity();
        e.setId(id);
        e.setBaseName("Virgo Shaka");
        e.setOriginalName("Virgo Shaka");
        e.setGroup(Group.GOLD);
        e.setRevival(true);
        when(repository.findById(id)).thenReturn(Optional.of(e));

        CharacterFigure cf = new CharacterFigure();
        cf.setId(id);
        cf.setBaseName("Virgo Shaka");
        cf.setOriginalName("Virgo Shaka");
        cf.setGroup(Group.GOLD);
        cf.setRevival(true);
        cf.setTags(Set.of("ex", "virgo"));
        when(modelMapper.toModel(any(CharacterFigureEntity.class))).thenReturn(cf);

        // the tags are provided.
        CharacterFigure actual = service.updateTagsInCharacter(id, Set.of("ex", "virgo"));
        assertNotNull(actual);
        assertEquals("65215079a5d1a04590202d6f", actual.getId());
        assertNull(actual.getOriginalName());
        assertNull(actual.getBaseName());
        assertEquals("Virgo Shaka", actual.getDisplayableName());
        assertNull(actual.getLineUp());
        assertNull(actual.getSeries());
        assertEquals(Group.GOLD, actual.getGroup());
        assertFalse(actual.isMetalBody());
        assertFalse(actual.isOce());
        assertTrue(actual.isRevival());
        assertFalse(actual.isPlainCloth());
        assertFalse(actual.isBrokenCloth());
        assertFalse(actual.isBronzeToGold());
        assertFalse(actual.isGold());
        assertFalse(actual.isHongKongVersion());
        assertFalse(actual.isManga());
        assertFalse(actual.isSurplice());
        assertFalse(actual.isSet());
        assertNull(actual.getAnniversary());
        assertNull(actual.getRestocks());
        assertNull(actual.getIssuanceJPY());
        assertNull(actual.getIssuanceMXN());
        assertFalse(actual.isFutureRelease());
        assertNull(actual.getUrl());
        assertNull(actual.getDistribution());
        assertNull(actual.getRemarks());
        assertNotNull(actual.getTags());
        assertEquals(Set.of("ex", "virgo"), actual.getTags());
    }

    /**
     * Test for {@link CharacterFigureService#updateTagsInCharacter(String, Set)}
     */
    @Test
    public void should_update_tags_when_existing_tags_and_new_tags_are_provided() {
        final String id = "65215079a5d1a04590202d6f";

        CharacterFigureEntity entity = createFigureEntity(id, "Virgo Shaka", "Virgo Shaka", Group.GOLD, true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            CharacterFigureEntity e = (CharacterFigureEntity) args[0];
            assertEquals(id, e.getId());
            assertEquals("Virgo Shaka", e.getBaseName());
            assertEquals("Virgo Shaka", e.getOriginalName());
            assertEquals(Group.GOLD, e.getGroup());
            assertTrue(e.isRevival());

            return null;
        }).when(repository).save(any(CharacterFigureEntity.class));

        CharacterFigureEntity e = new CharacterFigureEntity();
        e.setId(id);
        e.setBaseName("Virgo Shaka");
        e.setOriginalName("Virgo Shaka");
        e.setGroup(Group.GOLD);
        e.setRevival(true);
        e.setTags(Set.of("shaka")); // existing tags provided.
        when(repository.findById(id)).thenReturn(Optional.of(e));

        CharacterFigure cf = new CharacterFigure();
        cf.setId(id);
        cf.setBaseName("Virgo Shaka");
        cf.setOriginalName("Virgo Shaka");
        cf.setGroup(Group.GOLD);
        cf.setRevival(true);
        cf.setTags(Set.of("ex", "virgo", "shaka"));
        when(modelMapper.toModel(any(CharacterFigureEntity.class))).thenReturn(cf);

        // the tags are provided.
        CharacterFigure actual = service.updateTagsInCharacter(id, Set.of("ex", "virgo"));
        assertNotNull(actual);
        assertEquals("65215079a5d1a04590202d6f", actual.getId());
        assertNull(actual.getOriginalName());
        assertNull(actual.getBaseName());
        assertEquals("Virgo Shaka", actual.getDisplayableName());
        assertNull(actual.getLineUp());
        assertNull(actual.getSeries());
        assertEquals(Group.GOLD, actual.getGroup());
        assertFalse(actual.isMetalBody());
        assertFalse(actual.isOce());
        assertTrue(actual.isRevival());
        assertFalse(actual.isPlainCloth());
        assertFalse(actual.isBrokenCloth());
        assertFalse(actual.isBronzeToGold());
        assertFalse(actual.isGold());
        assertFalse(actual.isHongKongVersion());
        assertFalse(actual.isManga());
        assertFalse(actual.isSurplice());
        assertFalse(actual.isSet());
        assertNull(actual.getAnniversary());
        assertNull(actual.getRestocks());
        assertNull(actual.getIssuanceJPY());
        assertNull(actual.getIssuanceMXN());
        assertFalse(actual.isFutureRelease());
        assertNull(actual.getUrl());
        assertNull(actual.getDistribution());
        assertNull(actual.getRemarks());
        assertNotNull(actual.getTags());
        assertEquals(Set.of("ex", "virgo", "shaka"), actual.getTags());
    }

    /**
     * Test for {@link CharacterFigureService#deleteAllTagsInCharacter(String)}
     */
    @Test
    public void should_fail_character_delete_tags_when_id_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.deleteAllTagsInCharacter(null));
        assertEquals("Provide a non empty character id", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#deleteAllTagsInCharacter(String)}
     */
    @Test
    public void should_fail_character_delete_tags_when_id_not_found() {
        final String id = "223"; // not found
        CharacterFigureNotFoundException exception = assertThrows(CharacterFigureNotFoundException.class,
                () -> service.deleteAllTagsInCharacter(id));
        assertEquals("Character not found with id: 223", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFigureService#deleteAllTagsInCharacter(String)}
     */
    @Test
    public void should_delete_tags_when_character_id_is_found() {
        final String id = "65215079a5d1a04590202d6f";

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            CharacterFigureEntity e = (CharacterFigureEntity) args[0];
            assertEquals(id, e.getId());
            assertEquals("Virgo Shaka", e.getBaseName());
            assertEquals("Virgo Shaka", e.getOriginalName());
            assertEquals(Group.GOLD, e.getGroup());
            assertTrue(e.isRevival());
            assertNull(e.getTags()); // the tags are null

            return null;
        }).when(repository).save(any(CharacterFigureEntity.class));

        CharacterFigureEntity e = new CharacterFigureEntity();
        e.setId(id);
        e.setBaseName("Virgo Shaka");
        e.setOriginalName("Virgo Shaka");
        e.setGroup(Group.GOLD);
        e.setRevival(true);
        e.setTags(Set.of("ex", "virgo"));
        when(repository.findById(id)).thenReturn(Optional.of(e));

        CharacterFigure cf = new CharacterFigure();
        cf.setId(id);
        cf.setBaseName("Virgo Shaka");
        cf.setOriginalName("Virgo Shaka");
        cf.setGroup(Group.GOLD);
        cf.setRevival(true);
        cf.setTags(null);
        when(modelMapper.toModel(any(CharacterFigureEntity.class))).thenReturn(cf);

        // the tags are deleted.
        CharacterFigure actual = service.deleteAllTagsInCharacter(id);
        assertNotNull(actual);
        assertEquals("65215079a5d1a04590202d6f", actual.getId());
        assertNull(actual.getOriginalName());
        assertNull(actual.getBaseName());
        assertEquals("Virgo Shaka", actual.getDisplayableName());
        assertNull(actual.getLineUp());
        assertNull(actual.getSeries());
        assertEquals(Group.GOLD, actual.getGroup());
        assertFalse(actual.isMetalBody());
        assertFalse(actual.isOce());
        assertTrue(actual.isRevival());
        assertFalse(actual.isPlainCloth());
        assertFalse(actual.isBrokenCloth());
        assertFalse(actual.isBronzeToGold());
        assertFalse(actual.isGold());
        assertFalse(actual.isHongKongVersion());
        assertFalse(actual.isManga());
        assertFalse(actual.isSurplice());
        assertFalse(actual.isSet());
        assertNull(actual.getAnniversary());
        assertNull(actual.getRestocks());
        assertNull(actual.getIssuanceJPY());
        assertNull(actual.getIssuanceMXN());
        assertFalse(actual.isFutureRelease());
        assertNull(actual.getUrl());
        assertNull(actual.getDistribution());
        assertNull(actual.getRemarks());
        assertNull(actual.getTags());
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
