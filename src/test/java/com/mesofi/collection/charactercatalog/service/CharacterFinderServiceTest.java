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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFinderService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFinderServiceTest {

    @Mock
    private CharacterFigureRepository repository;
    @Mock
    private CharacterFigureService characterFigureService;

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
}
