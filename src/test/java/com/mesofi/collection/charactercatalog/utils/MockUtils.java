/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Jan 31, 2024.
 */
package com.mesofi.collection.charactercatalog.utils;

import com.mesofi.collection.charactercatalog.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author armandorivasarzaluz
 */
public class MockUtils {
  public static CharacterFigure createBasicMcCharacterFigure(
      String id, String name, BigDecimal basePrice, LocalDate releaseDate) {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setId(id);
    characterFigure.setOriginalName(name);
    characterFigure.setBaseName(name);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH);
    characterFigure.setSeries(Series.SAINT_SEIYA);
    characterFigure.setGroup(Group.GOLD);
    Issuance issuance = new Issuance();
    issuance.setBasePrice(basePrice);
    issuance.setReleaseDate(releaseDate);
    characterFigure.setIssuanceJPY(issuance);
    return characterFigure;
  }
}
