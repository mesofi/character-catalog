/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapperImpl;
import com.mesofi.collection.charactercatalog.mock.CharacterFigureCustomRepositoryImpl;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

/**
 * Test for {@link CharacterFinderService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
// @formatter:off
@ContextConfiguration(classes = {
        CharacterFinderService.class,
        CharacterFigureCustomRepositoryImpl.class,
        CharacterFigureService.class,
        CharacterFigureModelMapperImpl.class,
        CharacterFigureFileMapper.class })
// @formatter:on
// @DisplayName("Testing different ways to unify character names")
public class CharacterFinderServiceTest {

    @Mock
    private CharacterFigureRepository repository;
    @Mock
    private CharacterFigureService characterFigureService;

    private CharacterFinderService service;

    private static List<CharacterFigureEntity> entities = new ArrayList<>();

    private final String CSV = ".csv";
    private final String PREFIX = "should_find: ";
    private final String EX_LOCATION = "/lineup/myth_cloth_ex/";

    private final String GEMINI_SAGA_24K = "Gemini Saga Gold 24K EX";
    private final String GEMINI_SAGA_EX_SET = "Gemini Saga (God Cloth) Saga Saga Premium Set EX";

    @BeforeAll
    public static void initAll(@Autowired CharacterFigureService characterFigureRealService) throws IOException {
        Resource resource = new ClassPathResource("characters/MythCloth Catalog - CatalogMyth.tsv");

        // We assign a fake id to the records just loaded.
        // @formatter:off
        entities = characterFigureRealService.convertStreamToEntityList(resource.getInputStream())
                .stream().peek($ -> $.setId(new UID().toString()))
                .collect(Collectors.toList());
        // @formatter:on
    }

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

    @ParameterizedTest
    @DisplayName(PREFIX + GEMINI_SAGA_EX_SET)
    @CsvFileSource(resources = EX_LOCATION + GEMINI_SAGA_EX_SET + CSV, numLinesToSkip = 1)
    public void should_match_gemini_saga_ex_set(final String input) {
        shouldMatchCharacterFigure(input, GEMINI_SAGA_EX_SET);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + GEMINI_SAGA_24K)
    @CsvFileSource(resources = EX_LOCATION + GEMINI_SAGA_24K + CSV, numLinesToSkip = 1)
    public void should_match_gemini_saga_24k_ex(final String input) {
        shouldMatchCharacterFigure(input, GEMINI_SAGA_24K);
    }

    /**
     * Test for {@link CharacterFinderService#findCharacterByName(String)}
     */
    private void shouldMatchCharacterFigure(final String input, final String originalName) {

        entities.stream().filter($ -> originalName.equals($.getOriginalName())).findFirst().ifPresentOrElse((ddd) -> {
            CharacterFigure cf = new CharacterFigure();
            cf.setOriginalName(originalName);

            when(repository.findAll()).thenReturn(entities);
            when(characterFigureService.fromEntityToDisplayableFigure(any(CharacterFigureEntity.class))).thenReturn(cf);

            List<CharacterFigure> characterFigureList = service.findCharacterByName(input);
            assertNotNull(characterFigureList);
            assertFalse(characterFigureList.isEmpty());
            assertEquals(1, characterFigureList.size());
            assertEquals(originalName, characterFigureList.get(0).getOriginalName());
        }, () -> fail("No character found with original name: " + originalName));
    }
}
