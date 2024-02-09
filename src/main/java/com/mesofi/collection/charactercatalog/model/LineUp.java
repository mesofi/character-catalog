/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 23, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

/**
 * The LineUp enum.
 *
 * @author armandorivasarzaluz
 */
@Getter
public enum LineUp {

  // @formatter:off
  MYTH_CLOTH("Myth Cloth"),
  MYTH_CLOTH_EX("Myth Cloth EX"),
  APPENDIX("Appendix"),
  CROWN("Saint Cloth Crown"),
  LEGEND("Saint Cloth Legend"),
  DDP("DD Panoramation"),
  FIGUARTS("Figuarts");
  // @formatter:on

  private final String description;

  LineUp(final String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return description;
  }
}
