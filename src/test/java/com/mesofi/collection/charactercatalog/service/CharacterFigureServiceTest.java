/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.mock.MockMultipartFile;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFigureService}
 * 
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {

    private CharacterFigureService service;

    @Mock
    private CharacterFigureRepository repo;
    @Mock
    private CharacterFigureModelMapper modelMapper;
    @Mock
    private CharacterFigureFileMapper fileMapper;

    @BeforeEach
    public void init() {
        service = new CharacterFigureService(repo, modelMapper, fileMapper);
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
    public void should_fail_when_file_is_unable_to_read() {
        MockMultipartFile multipartFile = new MockMultipartFile();
        CharacterFigureException exception = assertThrows(CharacterFigureException.class,
                () -> service.loadAllCharacters(multipartFile));
        assertEquals("Unable to read characters from file", exception.getMessage());
    }
}
