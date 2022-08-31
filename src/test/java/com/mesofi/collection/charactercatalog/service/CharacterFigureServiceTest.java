package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.MockData.ACHERON_KARON;
import static com.mesofi.collection.charactercatalog.MockData.ARIES_MU_OCE;
import static com.mesofi.collection.charactercatalog.MockData.DRAGON_SHIRYU_GOLDEN_LIMITED;
import static com.mesofi.collection.charactercatalog.MockData.EX_ARIES_MU_OCE;
import static com.mesofi.collection.charactercatalog.MockData.EX_DRAGON_SHIRYU_GOLDEN_LIMITED;
import static com.mesofi.collection.charactercatalog.MockData.EX_GEMINI_SAGA_GOLD24;
import static com.mesofi.collection.charactercatalog.MockData.EX_GEMINI_SAGA_REV;
import static com.mesofi.collection.charactercatalog.MockData.EX_PISCES_APHRODITE_OCE;
import static com.mesofi.collection.charactercatalog.MockData.EX_SAGA_SAGA_SET;
import static com.mesofi.collection.charactercatalog.MockData.EX_TAURUS_ALDEBARAN_SOG;
import static com.mesofi.collection.charactercatalog.MockData.GEMINI_SAGA_GOLD24;
import static com.mesofi.collection.charactercatalog.MockData.GEMINI_SAGA_REV;
import static com.mesofi.collection.charactercatalog.MockData.PISCES_APHRODITE_OCE;
import static com.mesofi.collection.charactercatalog.MockData.SAGA_SAGA_SET;
import static com.mesofi.collection.charactercatalog.MockData.TAURUS_ALDEBARAN_SOG;
import static com.mesofi.collection.charactercatalog.MockData._ACHERON_KARON;
import static com.mesofi.collection.charactercatalog.MockData.createBasicEXCharacterFigure;
import static com.mesofi.collection.charactercatalog.MockData.createBasicRestock;
import static com.mesofi.collection.charactercatalog.model.LineUp.MYTH_CLOTH_EX;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.mesofi.collection.charactercatalog.MockData;
import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.exceptions.NoSuchCharacterFoundException;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Restock;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;
import com.mesofi.collection.charactercatalog.repository.CharacterUpdatableRepository;
import com.mongodb.client.result.UpdateResult;

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
    @CsvFileSource(resources = _ACHERON_KARON, numLinesToSkip = 1)
    public void isAcheronKaron_ShouldReturnTrueForAcheronKaron(final String name) {
        testCharacterFigure(ACHERON_KARON, name, LineUp.MYTH_CLOTH);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_ARIES_MU_OCE, numLinesToSkip = 1)
    public void isAriesMu_ShouldReturnTrueForAriesMu_OCE_EX(final String name) {
        testCharacterFigure(ARIES_MU_OCE, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_DRAGON_SHIRYU_GOLDEN_LIMITED, numLinesToSkip = 1)
    public void isDragonShiryu_ShouldReturnTrueForDragonShiryu_Limited_EX(final String name) {
        testCharacterFigure(DRAGON_SHIRYU_GOLDEN_LIMITED, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_SAGA_SAGA_SET, numLinesToSkip = 1)
    public void isSagaSaga_ShouldReturnTrueForSaga_EX(final String name) {
        testCharacterFigure(SAGA_SAGA_SET, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_GEMINI_SAGA_REV, numLinesToSkip = 1)
    public void isGeminiSaga_ShouldReturnTrueForGeminiSaga_Revival_EX(final String name) {
        testCharacterFigure(GEMINI_SAGA_REV, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_GEMINI_SAGA_GOLD24, numLinesToSkip = 1)
    public void isSagaGold24_ShouldReturnTrueForSagaGold24_EX(final String name) {
        testCharacterFigure(GEMINI_SAGA_GOLD24, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_PISCES_APHRODITE_OCE, numLinesToSkip = 1)
    public void isPiscesAphrodite_ShouldReturnTrueForPiscesAphrodite_OCE_EX(final String name) {
        testCharacterFigure(PISCES_APHRODITE_OCE, name, MYTH_CLOTH_EX);
    }

    @ParameterizedTest
    @CsvFileSource(resources = EX_TAURUS_ALDEBARAN_SOG, numLinesToSkip = 1)
    public void isTaurusSOG_ShouldReturnTrueForTaurusSOG_EX(final String name) {
        testCharacterFigure(TAURUS_ALDEBARAN_SOG, name, MYTH_CLOTH_EX);
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

    @Test
    public void should_AssertIdWithError_WhenIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // method to be tested
            characterFigureService.updateCharacterRestock(null, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals("Provide a valid id for the character to update", actualMessage);
    }

    @Test
    public void should_AssertIdWithError_WhenRestocksIsNull() {
        String id = "62d4658bff17ae100e217e50";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // method to be tested
            characterFigureService.updateCharacterRestock(id, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals("Provide a valid list of restocks", actualMessage);
    }

    @Test
    public void should_AssertIdWithError_WhenCharacterWasNotFound() {
        List<Restock> restocks = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, Calendar.JANUARY, 8, 0, 0, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022, Calendar.JULY, 8, 0, 0, 0);

        restocks.add(createBasicRestock(calendar1.getTime(), "https://tamashii.jp/item/13721/", "Some comment"));
        restocks.add(createBasicRestock(calendar2.getTime(), "https://tamashii.jp/item/13333/", "Another comment"));

        String id = "9999999";

        when(characterUpdatableRepository.updateRestocks(id, restocks)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchCharacterFoundException.class, () -> {
            // method to be tested
            characterFigureService.updateCharacterRestock(id, restocks);
        });
        String actualMessage = exception.getMessage();
        assertEquals("Character not found: 9999999", actualMessage);
    }

    @Test
    public void should_AssertTrue_WhenDataIsProvided() {
        List<Restock> restocks = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, Calendar.JANUARY, 8, 0, 0, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022, Calendar.JULY, 8, 0, 0, 0);

        restocks.add(createBasicRestock(calendar1.getTime(), "https://tamashii.jp/item/13721/", "Some comment"));
        restocks.add(createBasicRestock(calendar2.getTime(), "https://tamashii.jp/item/13333/", "Another comment"));

        String id = "62d4658bff17ae100e217e50";

        Optional<UpdateResult> result = Optional.of(new UpdateResult() {

            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            @Override
            public BsonValue getUpsertedId() {
                return null;
            }

            @Override
            public long getModifiedCount() {
                return 1;
            }

            @Override
            public long getMatchedCount() {
                return 1;
            }
        });

        Optional<CharacterFigure> characterOptional = Optional
                .of(createBasicEXCharacterFigure(id, "Shun", new BigDecimal("4000")));

        when(characterUpdatableRepository.updateRestocks(id, restocks)).thenReturn(result);
        when(characterRepository.findById(id)).thenReturn(characterOptional);

        // method to be tested
        CharacterFigure characterFigure = characterFigureService.updateCharacterRestock(id, restocks);
        assertNotNull(characterFigure);
        assertEquals("62d4658bff17ae100e217e50", characterFigure.getId());
        assertEquals("Shun", characterFigure.getName());
        assertThat("Thu Oct 27 11:11:11 CDT 2022", containsString("Thu Oct 27 11:11:11"));
        assertEquals("4000", characterFigure.getBasePrice().toString());
        assertEquals("99.99", characterFigure.getPrice().toString());
        assertEquals("0.10", characterFigure.getTax().toString());
        assertEquals("ex", characterFigure.getLineUp().toString());
        assertEquals("GENERAL", characterFigure.getDistribution().name());
        assertNull(characterFigure.getUrl());
        assertNull(characterFigure.getRestocks());
    }
}
