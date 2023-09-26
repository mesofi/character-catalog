/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
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
public class CharacterFigureServiceTest {

    @Mock
    private CharacterFigureRepository characterFigureRepository;
    @Mock
    private CharacterFigureModelMapper modelMapper;
    @Mock
    private CharacterFigureFileMapper fileMapper;

    private CharacterFigureService characterFigureService;

    @BeforeEach
    public void init() {
        characterFigureService = new CharacterFigureService(characterFigureRepository, modelMapper, fileMapper);
    }

    @Test
    public void should_fail_when_input_file_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> characterFigureService.loadAllCharacters(null));
        assertEquals("The uploaded file is missing...", exception.getMessage());
    }

    @Test
    public void should_load_all_records() throws IOException {

        doNothing().when(characterFigureRepository).deleteAll();
        when(fileMapper.fromLineToCharacterFigure(anyString())).thenReturn(new CharacterFigure());

        final String folder = "characters/";
        final String name = "MythCloth Catalog - CatalogMyth-min.tsv";
        final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));
        MultipartFile result = new MockMultipartFile(name, name, "text/plain", bytes);

        characterFigureService.loadAllCharacters(result);
    }

    @Test
    public void should_get_effective_characters_with_empty_records() {
        List<CharacterFigure> list = characterFigureService.getEffectiveCharacters(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void should_get_effective_characters_with_restocks() {
        List<CharacterFigure> allCharacters = new ArrayList<>();
        allCharacters.add(createCharacter("Libra Dohko (God Cloth)", LocalDate.of(2021, 9, 18), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false));
        allCharacters.add(createCharacter("Libra Dohko (God Cloth)", LocalDate.of(2018, 1, 27), LineUp.MYTH_CLOTH_EX,
                Series.SOG, Group.GOLD, false, false));

        List<CharacterFigure> list = characterFigureService.getEffectiveCharacters(allCharacters);
        assertNotNull(list);
    }

    private CharacterFigure createCharacter(String originalName, LocalDate releasePrice, LineUp lineUp, Series series,
            Group group, boolean oce, boolean revival) {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setOriginalName(originalName);
        characterFigure.setReleaseConfirmationDay(true);
        characterFigure.setReleaseDate(releasePrice);
        characterFigure.setLineUp(lineUp);
        characterFigure.setSeries(series);
        characterFigure.setGroup(group);
        characterFigure.setOce(oce);
        characterFigure.setRevival(revival);

        return characterFigure;
    }
}
