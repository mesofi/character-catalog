package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.MockData.SAGA_GOLD24;
import static com.mesofi.collection.charactercatalog.MockData.SAGA_SAGA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.Yaml;

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

    private static final String DB_PATH = "src/test/resources/mongodb/character-figure.json";
    private static List<CharacterFigure> allCharacters = new ArrayList<>();

    private CharacterFigureService characterFigureService;

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUpdatableRepository characterUpdatableRepository;

    @Value("${character.keyword-exclude}")
    private List<String> ordersBatchSize;

    @BeforeAll
    public static void beforeAll() throws Exception {
        allCharacters = MockData.loadAllCharacters(DB_PATH);
    }

    @BeforeEach
    public void init() {
        CharacterConfig config = loadConfigFile("application-test.yml");
        characterFigureService = new CharacterFigureService(config, characterRepository, characterUpdatableRepository);
    }

    @SuppressWarnings("unchecked")
    private CharacterConfig loadConfigFile(String configLocation) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocation);
        Map<String, Object> map = (Map<String, Object>) yaml.loadAs(inputStream, Map.class);

        Map<String, Object> characterMap = (Map<String, Object>) map.get("character");
        String symbols = (String) characterMap.get("symbol-exclude");
        List<String> keywords = (ArrayList<String>) characterMap.get("keyword-exclude");

        CharacterConfig config = new CharacterConfig();
        config.setSymbolExclude(symbols);
        config.setKeywordExclude(keywords);

        return config;
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/" + SAGA_GOLD24 + "/data.csv", numLinesToSkip = 1)
    public void isSagaGold24_ShouldReturnTrueForSagaGold24(String name) {
        testCharacterFigure(SAGA_GOLD24, name);
    }

    // @ParameterizedTest
    // @CsvFileSource(resources = "/" + SAGA_SAGA + "/data.csv", numLinesToSkip = 1)
    public void isSagaSaga_ShouldReturnTrueForSaga(String name) {
        testCharacterFigure(SAGA_SAGA, name);
    }

    private void testCharacterFigure(String expectedName, String actualName) {
        when(characterRepository.findAllBylineUp(LineUp.MYTH_CLOTH_EX)).thenReturn(allCharacters);

        // method to be tested
        Optional<CharacterFigure> result = characterFigureService.retrieveCharacterByName(actualName);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedName, result.get().getName());
    }
}
