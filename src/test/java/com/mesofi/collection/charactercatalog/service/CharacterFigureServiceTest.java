package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.MockData.ARIES_MU_OCE;
import static com.mesofi.collection.charactercatalog.MockData.EX_ARIES_MU_OCE;
//import static com.mesofi.collection.charactercatalog.MockData.SAGA_SAGA;
import static com.mesofi.collection.charactercatalog.MockData.EX_GEMINI_SAGA_GOLD24;
import static com.mesofi.collection.charactercatalog.MockData.EX_SAGA_SAGA_SET;
import static com.mesofi.collection.charactercatalog.MockData.EX_TAURUS_ALDEBARAN_SOG;
import static com.mesofi.collection.charactercatalog.MockData.GEMINI_SAGA_GOLD24;
import static com.mesofi.collection.charactercatalog.MockData.SAGA_SAGA_SET;
import static com.mesofi.collection.charactercatalog.MockData.TAURUS_ALDEBARAN_SOG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.mesofi.collection.charactercatalog.MockData;
import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;
import com.mesofi.collection.charactercatalog.repository.CharacterUpdatableRepository;

/**
 * Testing for {@link CharacterFigureService}
 * 
 * @author armandorivasarzaluz
 *
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {

    private static final String CONFIG_PATH = "application-test.yml";
    private static final String DB_PATH = "src/test/resources/mongodb/character-figure.json";
    private static CharacterConfig config;
    private static List<CharacterFigure> allCharacters;

    private CharacterFigureService characterFigureService;

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUpdatableRepository characterUpdatableRepository;

    @BeforeAll
    public static void beforeAll() throws Exception {
        config = MockData.loadConfigFile(CONFIG_PATH);
        allCharacters = MockData.loadAllCharacters(DB_PATH);
    }

    @BeforeEach
    public void init() {
        characterFigureService = new CharacterFigureService(config, characterRepository, characterUpdatableRepository);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_ARIES_MU_OCE, numLinesToSkip = 1)
    public void isAriesMu_ShouldReturnTrueForAriesMu_OCE_EX(final String name) {
        testCharacterFigure(ARIES_MU_OCE, name, LineUp.MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_SAGA_SAGA_SET, numLinesToSkip = 1)
    public void isSagaSaga_ShouldReturnTrueForSaga_EX(final String name) {
        testCharacterFigure(SAGA_SAGA_SET, name, LineUp.MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_GEMINI_SAGA_GOLD24, numLinesToSkip = 1)
    public void isSagaGold24_ShouldReturnTrueForSagaGold24_EX(final String name) {
        testCharacterFigure(GEMINI_SAGA_GOLD24, name, LineUp.MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_TAURUS_ALDEBARAN_SOG, numLinesToSkip = 1)
    public void isTaurusSOG_ShouldReturnTrueForTaurusSOG_EX(final String name) {
        testCharacterFigure(TAURUS_ALDEBARAN_SOG, name, LineUp.MYTH_CLOTH_EX);
    }

    private void testCharacterFigure(final String expectedName, final String actualName, final LineUp lineUp) {
        when(characterRepository.findAllBylineUp(lineUp))
                .thenReturn(allCharacters.stream().filter($ -> $.getLineUp() == lineUp).collect(Collectors.toList()));

        // method to be tested
        Optional<CharacterFigure> result = characterFigureService.retrieveCharacterByName(actualName);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedName, result.get().getName());
    }
}
