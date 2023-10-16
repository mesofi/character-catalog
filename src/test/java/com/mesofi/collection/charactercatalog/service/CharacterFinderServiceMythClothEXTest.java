/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
import com.mesofi.collection.charactercatalog.test.LoggingUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Test for {@link CharacterFinderService}
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
// @formatter:off
@ContextConfiguration(classes = {
        CharacterFinderService.class,
        CharacterFigureCustomRepositoryImpl.class,
        CharacterFigureService.class,
        CharacterFigureModelMapperImpl.class,
        CharacterFigureFileMapper.class })
// @formatter:on
@DisplayName("CharacterFinder - Myth Cloth EX")
public class CharacterFinderServiceMythClothEXTest {

    @Mock
    private CharacterFigureRepository repository;
    @Mock
    private CharacterFigureService characterFigureService;

    private CharacterFinderService service;

    private static List<CharacterFigureEntity> entities = new ArrayList<>();

    private final String CSV = ".csv";
    private final String PREFIX = "should_find: ";
    private final String LOCATION = "/lineup/myth_cloth_ex/";

    private final String AQUARIUS_CAMUS_GOD = "Aquarius Camus (God Cloth) EX";
    private final String AQUARIUS_CAMUS_SURPLICE = "Aquarius Camus (Surplice) EX";
    private final String DRAGON_SHIRYU_V2_GOLDEN_HK = "Dragon Shiryu ~New Bronze Cloth~ Golden Limited Edition ~HK Version~ EX";
    private final String GEMINI_SAGA_SET = "Gemini Saga (God Cloth) Saga Saga Premium Set EX";
    private final String GEMINI_SAGA_REVIVAL = "Gemini Saga EX <Revival>";
    private final String GEMINI_SAGA_24K = "Gemini Saga Gold 24K EX";
    private final String SAGITTARIUS_SEIYA_GOLD = "Sagittarius Seiya Gold 24K EX";
    private final String TAURUS_ALDEBARAN_GOD = "Taurus Aldebaran (God Cloth) EX";
    private final String THIRD_GOLDEN_OCE = "The Third Golden Saint Warrior EX OCE";

    @BeforeAll
    public static void initAll(@Autowired CharacterFigureService characterFigureRealService) throws IOException {
        LoggingUtils.enableLog(log); // enabled DEBUG
        log.debug("Loading the whole DB so that we can test the names.");

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

    @ParameterizedTest
    @DisplayName(PREFIX + AQUARIUS_CAMUS_GOD)
    @CsvFileSource(resources = LOCATION + AQUARIUS_CAMUS_GOD + CSV, numLinesToSkip = 1)
    public void should_match_aquarius_camus_god(final String input) {
        shouldMatchCharacterFigure(input, AQUARIUS_CAMUS_GOD);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + AQUARIUS_CAMUS_SURPLICE)
    @CsvFileSource(resources = LOCATION + AQUARIUS_CAMUS_SURPLICE + CSV, numLinesToSkip = 1)
    public void should_match_aquarius_camus_surplice(final String input) {
        shouldMatchCharacterFigure(input, AQUARIUS_CAMUS_SURPLICE);
    }

    @Disabled("More than 1 occurrence ... how to deal with this?")
    @ParameterizedTest
    @DisplayName(PREFIX + DRAGON_SHIRYU_V2_GOLDEN_HK)
    @CsvFileSource(resources = LOCATION + DRAGON_SHIRYU_V2_GOLDEN_HK + CSV, numLinesToSkip = 1)
    public void should_match_dragon_shiryu_v2_golden_hk(final String input) {
        shouldMatchCharacterFigure(input, DRAGON_SHIRYU_V2_GOLDEN_HK, 2);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + GEMINI_SAGA_SET)
    @CsvFileSource(resources = LOCATION + GEMINI_SAGA_SET + CSV, numLinesToSkip = 1)
    public void should_match_gemini_saga_set(final String input) {
        shouldMatchCharacterFigure(input, GEMINI_SAGA_SET);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + GEMINI_SAGA_REVIVAL)
    @CsvFileSource(resources = LOCATION + GEMINI_SAGA_REVIVAL + CSV, numLinesToSkip = 1)
    public void should_match_gemini_saga_revival(final String input) {
        shouldMatchCharacterFigure(input, GEMINI_SAGA_REVIVAL);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + GEMINI_SAGA_24K)
    @CsvFileSource(resources = LOCATION + GEMINI_SAGA_24K + CSV, numLinesToSkip = 1)
    public void should_match_gemini_saga_24k(final String input) {
        shouldMatchCharacterFigure(input, GEMINI_SAGA_24K);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + SAGITTARIUS_SEIYA_GOLD)
    @CsvFileSource(resources = LOCATION + SAGITTARIUS_SEIYA_GOLD + CSV, numLinesToSkip = 1)
    public void should_match_sagittarius_seiya_gold(final String input) {
        shouldMatchCharacterFigure(input, SAGITTARIUS_SEIYA_GOLD);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + TAURUS_ALDEBARAN_GOD)
    @CsvFileSource(resources = LOCATION + TAURUS_ALDEBARAN_GOD + CSV, numLinesToSkip = 1)
    public void should_match_taurus_aldebaran_god(final String input) {
        shouldMatchCharacterFigure(input, TAURUS_ALDEBARAN_GOD);
    }

    @ParameterizedTest
    @DisplayName(PREFIX + THIRD_GOLDEN_OCE)
    @CsvFileSource(resources = LOCATION + THIRD_GOLDEN_OCE + CSV, numLinesToSkip = 1)
    public void should_match_third_golden_oce(final String input) {
        shouldMatchCharacterFigure(input, THIRD_GOLDEN_OCE);
    }

    /**
     * Test for {@link CharacterFinderService#findCharacterByName(String)}
     */
    private void shouldMatchCharacterFigure(final String input, final String originalName) {
        shouldMatchCharacterFigure(input, originalName, 1);
    }

    /**
     * Test for {@link CharacterFinderService#findCharacterByName(String)}
     */
    private void shouldMatchCharacterFigure(final String input, final String originalName, int expectedItems) {
        log.debug("==> Find: [{}] and expect: [{}] <==", input, originalName);

        entities.stream().filter($ -> originalName.equals($.getOriginalName())).findFirst().ifPresentOrElse((ddd) -> {
            CharacterFigure cf = new CharacterFigure();
            cf.setOriginalName(originalName);

            when(repository.findAll()).thenReturn(entities);
            when(characterFigureService.fromEntityToDisplayableFigure(any(CharacterFigureEntity.class))).thenReturn(cf);

            // call the service ...
            List<CharacterFigure> characterFigureList = service.findCharacterByName(input);
            assertNotNull(characterFigureList);
            assertFalse(characterFigureList.isEmpty());
            assertEquals(1, characterFigureList.size());
            assertEquals(originalName, characterFigureList.get(0).getOriginalName());
        }, () -> fail("No character found with original name: " + originalName));
    }
}
