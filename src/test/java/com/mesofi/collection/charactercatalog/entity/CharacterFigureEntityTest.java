/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test for {@link CharacterFigureEntity}
 *
 * @author armandorivasarzaluz
 */
public class CharacterFigureEntityTest {

  private CharacterFigureEntity characterFigureEntity;

  @BeforeEach
  public void beforeEach() {
    characterFigureEntity = new CharacterFigureEntity();
  }

  @ParameterizedTest
  @MethodSource("provideBooleanValues")
  public void should_verify_getter_setter_properties(
      boolean metal,
      boolean oce,
      boolean revival,
      boolean plainCloth,
      boolean brokenCloth,
      boolean golden,
      boolean gold,
      boolean hk,
      boolean manga,
      boolean surplice,
      boolean set) {
    characterFigureEntity.setId("3209320mj0uyg387");
    characterFigureEntity.setOriginalName("Seiya");
    characterFigureEntity.setBaseName("Seiya");
    characterFigureEntity.setLineUp(LineUp.APPENDIX);
    characterFigureEntity.setSeries(Series.LEGEND);
    characterFigureEntity.setGroup(Group.BLACK);
    characterFigureEntity.setMetal(metal);
    characterFigureEntity.setOce(oce);
    characterFigureEntity.setRevival(revival);
    characterFigureEntity.setPlainCloth(plainCloth);
    characterFigureEntity.setBrokenCloth(brokenCloth);
    characterFigureEntity.setGolden(golden);
    characterFigureEntity.setGold(gold);
    characterFigureEntity.setHk(hk);
    characterFigureEntity.setManga(manga);
    characterFigureEntity.setSurplice(surplice);
    characterFigureEntity.setSet(set);
    characterFigureEntity.setAnniversary(20);
    characterFigureEntity.setTags(Set.of("gold"));
    characterFigureEntity.setImages(List.of(new GalleryImage()));

    assertEquals("3209320mj0uyg387", characterFigureEntity.getId());
    assertEquals("Seiya", characterFigureEntity.getOriginalName());
    assertEquals("Seiya", characterFigureEntity.getBaseName());
    assertEquals(LineUp.APPENDIX, characterFigureEntity.getLineUp());
    assertEquals(Series.LEGEND, characterFigureEntity.getSeries());
    assertEquals(Group.BLACK, characterFigureEntity.getGroup());
    assertEquals(metal, characterFigureEntity.isMetal());
    assertEquals(oce, characterFigureEntity.isOce());
    assertEquals(revival, characterFigureEntity.isRevival());
    assertEquals(plainCloth, characterFigureEntity.isPlainCloth());
    assertEquals(brokenCloth, characterFigureEntity.isBrokenCloth());
    assertEquals(golden, characterFigureEntity.isGolden());
    assertEquals(gold, characterFigureEntity.isGold());
    assertEquals(hk, characterFigureEntity.isHk());
    assertEquals(manga, characterFigureEntity.isManga());
    assertEquals(surplice, characterFigureEntity.isSurplice());
    assertEquals(set, characterFigureEntity.isSet());
    assertEquals(20, characterFigureEntity.getAnniversary());
    assertEquals(Set.of("gold"), characterFigureEntity.getTags());
    assertEquals(List.of(new GalleryImage()), characterFigureEntity.getImages());
  }

  @Test
  public void should_verify_equality_1() {
    CharacterFigureEntity otherCharacterFigureEntity = characterFigureEntity;
    assertEquals(characterFigureEntity, otherCharacterFigureEntity);
  }

  @Test
  public void should_verify_equality_2() {
    assertFalse(characterFigureEntity.equals(null));
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void should_verify_equality_3() {
    assertFalse(characterFigureEntity.equals(""));
  }

  @Test
  public void should_verify_equality_4() {
    CharacterFigureEntity otherCharacterFigureEntity = new CharacterFigureEntity();
    otherCharacterFigureEntity.setId("1");

    characterFigureEntity.setId("2");
    assertFalse(characterFigureEntity.equals(otherCharacterFigureEntity));
  }

  @Test
  public void should_verify_equality_5() {
    CharacterFigureEntity otherCharacterFigureEntity = new CharacterFigureEntity();
    otherCharacterFigureEntity.setId("1");

    characterFigureEntity.setId("1");
    assertTrue(characterFigureEntity.equals(otherCharacterFigureEntity));
  }

  @Test
  public void should_get_hash_code() {
    assertEquals(992, characterFigureEntity.hashCode());
  }

  private static Stream<Arguments> provideBooleanValues() {
    // @formatter:off
    return Stream.of(
        Arguments.of(true, true, true, true, true, true, true, true, true, true, true),
        Arguments.of(false, false, false, false, false, false, false, false, false, false, false));
    // @formatter:on
  }
}
