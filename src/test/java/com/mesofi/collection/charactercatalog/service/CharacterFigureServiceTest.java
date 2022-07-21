package com.mesofi.collection.charactercatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.Yaml;

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

    private CharacterFigureService characterFigureService;

    private CharacterConfig config;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUpdatableRepository characterUpdatableRepository;

    @Value("${character.keyword-exclude}")
    private List<String> ordersBatchSize;

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
        List<String> keywords = (ArrayList<String>) characterMap.get("keyword-exclude");

        CharacterConfig config = new CharacterConfig();
        config.setKeywordExclude(keywords);

        return config;
    }

    @Test
    public void should_ReturnStatuses_WhenDataFound() {
        // method to be tested
        Optional<CharacterFigure> result = characterFigureService.retrieveCharacterByName("");
    }

    @Test
    public void should_ReturnStatuses_WhenDataFound2() {
        String mandarake = "Bandai Spirits Saint Seiya Myth Cloth EX Masami Kurumada Gemini Saga GOLD24 Tamashi Nation 2021";
        String okini = "Saint Cloth Myth EX Gemini Saga (GOLD24)";
        String yoyakunow = "MYTH CLOTH EX GEMINI SAGA GOLD24 \"SAINT SEIYA\"";

        List<CharacterFigure> list = populateExFigures();
        when(characterRepository.findAllBylineUp(LineUp.MYTH_CLOTH_EX)).thenReturn(list);

        // method to be tested
        Optional<CharacterFigure> result1 = characterFigureService.retrieveCharacterByName(mandarake);
        Optional<CharacterFigure> result2 = characterFigureService.retrieveCharacterByName(okini);
        Optional<CharacterFigure> result3 = characterFigureService.retrieveCharacterByName(yoyakunow);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);

        assertFalse(result1.isEmpty());
        assertFalse(result2.isEmpty());
        assertFalse(result3.isEmpty());

        assertEquals("Gemini Saga GOLD24", result1.get().getName());
        assertEquals(result1.get().getName(), result2.get().getName());
        assertEquals(result2.get().getName(), result3.get().getName());

    }

    private List<CharacterFigure> populateExFigures() {
        List<CharacterFigure> list = new ArrayList<>();
        list.add(createExFigure("Gemini Saga GOLD24"));
        list.add(createExFigure("Libra Dohko (Sacred Cloth)"));
        return list;
    }

    private CharacterFigure createExFigure(String name) {
        return CharacterFigure.builder().name(name).lineUp(LineUp.MYTH_CLOTH_EX).releaseDate(new Date()).build();
    }
}
