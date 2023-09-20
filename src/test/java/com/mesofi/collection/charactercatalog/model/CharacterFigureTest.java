/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CharacterFigureTest {

    @Test
    public void should_not_equals() {
        CharacterFigure characterFigure1 = new CharacterFigure();
        CharacterFigure characterFigure2 = new CharacterFigure();
        assertEquals(characterFigure1, characterFigure2);
    }
    
    @Test
    public void should_equals() {
        CharacterFigure characterFigure1 = new CharacterFigure();
        characterFigure1.setBaseName("Cancer Death Mask");
        characterFigure1.setLineUp(LineUp.APPENDIX);
        characterFigure1.setSeries(Series.SAINT_SEIYA);
        characterFigure1.setGroup(Group.GOD);
        
        CharacterFigure characterFigure2 = new CharacterFigure();
        characterFigure2.setBaseName("Cancer Death Mask");
        characterFigure2.setLineUp(LineUp.APPENDIX);
        characterFigure2.setSeries(Series.SAINT_SEIYA);
        characterFigure2.setGroup(Group.GOD);

        assertEquals(characterFigure1, characterFigure2);
    }
}
