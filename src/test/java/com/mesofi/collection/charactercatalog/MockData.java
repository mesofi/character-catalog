package com.mesofi.collection.charactercatalog;

import java.util.ArrayList;
import java.util.List;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.LineUp;

public class MockData {
    private static List<CharacterFigure> list = new ArrayList<>();
    public static final String SAGA_SAGA = "Gemini Saga (God Cloth) Saga Saga premium set";
    public static final String SAGA_GOLD24 = "Gemini Saga GOLD24";
    public static final String LIBRA_SOG = "Libra Dohko (Sacred Cloth)";

    static {
        list.add(CharacterFigure.builder().lineUp(LineUp.MYTH_CLOTH_EX).name(SAGA_SAGA).build());
        list.add(CharacterFigure.builder().lineUp(LineUp.MYTH_CLOTH_EX).name(SAGA_GOLD24).build());
        list.add(CharacterFigure.builder().lineUp(LineUp.MYTH_CLOTH_EX).name(LIBRA_SOG).build());
    }

    public static List<CharacterFigure> createBasicEXFigures() {
        return list;
    }
}
