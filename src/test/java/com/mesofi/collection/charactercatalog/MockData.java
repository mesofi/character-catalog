package com.mesofi.collection.charactercatalog;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.LineUp;

public class MockData {
    public static final String SAGA = "Gemini Saga";
    public static final String SAGA_SAGA = "Gemini Saga (God Cloth) Saga Saga premium set";
    public static final String SAGA_GOLD24 = "Gemini Saga GOLD24";
    public static final String LIBRA_SOG = "Libra Dohko (Sacred Cloth)";

    @SuppressWarnings("unchecked")
    public static List<CharacterFigure> loadAllCharacters(String dbPath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> characterItems = objectMapper.readValue(new File(dbPath), List.class);
        return characterItems.stream().map(MockData::mapCharacterFigure).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static CharacterFigure mapCharacterFigure(LinkedHashMap<String, Object> item) {
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
}
