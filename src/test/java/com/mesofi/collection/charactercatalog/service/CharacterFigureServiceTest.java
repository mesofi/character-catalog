/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureMapper;
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
    private CharacterFigureMapper characterFigureMapper;

    private CharacterFigureService characterFigureService;

    @BeforeEach
    public void init() {
        characterFigureService = new CharacterFigureService(characterFigureRepository, characterFigureMapper);
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

        final String folder = "characters/";
        final String name = "MythCloth Catalog - CatalogMyth-min.tsv";
        final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));

        MultipartFile result = new MockMultipartFile(name, name, "text/plain", bytes);

        characterFigureService.loadAllCharacters(result);

    }
}
