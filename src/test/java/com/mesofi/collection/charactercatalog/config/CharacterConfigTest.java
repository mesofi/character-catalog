package com.mesofi.collection.charactercatalog.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Testing for {@link CharacterConfig}
 * 
 * @author armandorivasarzaluz
 *
 */
public class CharacterConfigTest {
    @Test
    public void getterAndSetterCharacterConfig() {
        CharacterConfig characterConfig = new CharacterConfig();
        characterConfig.setSymbolExclude("a");

        assertEquals("a", characterConfig.getSymbolExclude());
    }
}
