package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Test for {@link LineUp} */
public class LineUpTest {

  @Test
  public void should_return_lineup_names() {
    assertEquals("Myth Cloth", LineUp.MYTH_CLOTH.getDescription());
    assertEquals("Myth Cloth EX", LineUp.MYTH_CLOTH_EX.getDescription());
    assertEquals("Appendix", LineUp.APPENDIX.getDescription());
    assertEquals("Saint Cloth Crown", LineUp.CROWN.getDescription());
    assertEquals("Saint Cloth Legend", LineUp.LEGEND.getDescription());
    assertEquals("DD Panoramation", LineUp.DDP.getDescription());
    assertEquals("Figuarts", LineUp.FIGUARTS.getDescription());

    assertEquals("Myth Cloth", LineUp.MYTH_CLOTH.toString());
    assertEquals("Myth Cloth EX", LineUp.MYTH_CLOTH_EX.toString());
    assertEquals("Appendix", LineUp.APPENDIX.toString());
    assertEquals("Saint Cloth Crown", LineUp.CROWN.toString());
    assertEquals("Saint Cloth Legend", LineUp.LEGEND.toString());
    assertEquals("DD Panoramation", LineUp.DDP.toString());
    assertEquals("Figuarts", LineUp.FIGUARTS.toString());
  }
}
