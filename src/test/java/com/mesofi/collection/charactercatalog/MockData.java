package com.mesofi.collection.charactercatalog;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.LineUp;

public class MockData {
    public static final String EX_LINE_UP = "/lineup/mythclothex/";
    public static final String DATA_CSV = "/data.csv";

    public static final String GEMINI_SAGA_GOLD24 = "Gemini Saga GOLD24";
    public static final String EX_GEMINI_SAGA_GOLD24 = EX_LINE_UP + GEMINI_SAGA_GOLD24 + DATA_CSV;

    public static final String SAGA_SAGA_SET = "Gemini Saga (God Cloth) Saga Saga premium set";
    public static final String EX_SAGA_SAGA_SET = EX_LINE_UP + GEMINI_SAGA_GOLD24 + DATA_CSV;

    @SuppressWarnings("unchecked")
    public static List<CharacterFigure> loadAllCharacters(String dbPath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> characterItems = objectMapper.readValue(new File(dbPath), List.class);
        return characterItems.stream().map(MockData::mapCharacterFigure).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static CharacterFigure mapCharacterFigure(final LinkedHashMap<String, Object> item) {
        CharacterFigure characterFigure = new CharacterFigure();

        LinkedHashMap<String, String> idMap = (LinkedHashMap<String, String>) item.get("_id");
        LinkedHashMap<String, Object> releaseDateMap = (LinkedHashMap<String, Object>) item.get("releaseDate");
        LinkedHashMap<String, Object> dateMap = (LinkedHashMap<String, Object>) releaseDateMap.get("$date");

        characterFigure.setId(idMap.get("$oid"));
        characterFigure.setName((String) item.get("name"));
        characterFigure.setReleaseDate(new Date(Long.parseLong((String) dateMap.get("$numberLong"))));
        characterFigure.setBasePrice(new BigDecimal((String) item.get("basePrice")));
        characterFigure.setTax(new BigDecimal((String) item.get("tax")));
        characterFigure.setLineUp(LineUp.valueOf((String) item.get("lineUp")));
        characterFigure.setDistribution(Distribution.valueOf((String) item.get("distribution")));
        characterFigure.setUrl((String) item.get("url"));

        return characterFigure;
    }

    @SuppressWarnings("unchecked")
    public static CharacterConfig loadConfigFile(final String configLocation) {
        Yaml yaml = new Yaml();
        InputStream inputStream = MockData.class.getClassLoader().getResourceAsStream(configLocation);
        Map<String, Object> map = (Map<String, Object>) yaml.loadAs(inputStream, Map.class);

        Map<String, Object> characterMap = (Map<String, Object>) map.get("character");
        String symbols = (String) characterMap.get("symbol-exclude");
        List<String> keywords = (ArrayList<String>) characterMap.get("keyword-exclude");

        CharacterConfig config = new CharacterConfig();
        config.setSymbolExclude(symbols);
        config.setKeywordExclude(keywords);

        return config;
    }
}
