/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFinderService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CharacterFinderServiceTest {

    @Mock
    private CharacterFigureRepository repository;
    @Mock
    private CharacterFigureService characterFigureService;
    @Mock
    private CharacterFigureModelMapper modelMapper;
    @Mock
    private CharacterFigureFileMapper fileMapper;

    private CharacterFinderService service;

    @BeforeEach
    public void init() {
        service = new CharacterFinderService(repository, characterFigureService);
    }

    /**
     * Test for {@link CharacterFinderService#findCharacterByName(String)}
     */
    @Test
    public void should_fail_finding_figure_when_input_name_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.findCharacterByName(null));
        assertEquals("Provide a non empty figure name.", exception.getMessage());
    }

    /**
     * Test for {@link CharacterFinderService#findCharacterByName(String)}
     */
    //@ParameterizedTest
    //@CsvFileSource(resources = "/lineup/myth_cloth_ex/Gemini Saga (God Cloth) Saga Saga Premium Set.csv", numLinesToSkip = 1)
    public void should_fail_finding_figure_when_input_name_is_missing_(final String input) {
        service.findCharacterByName(input);
    }
} 
