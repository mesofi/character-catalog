/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 23, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import lombok.Getter;

/**
 * The Series enum.
 *
 * @author armandorivasarzaluz
 */
@Getter
public enum Series {

  // @formatter:off
  SAINT_SEIYA("Saint Seiya"),
  SOG("Soul of Gold"),
  OMEGA("Saint Seiya Omega"),
  SAINTIA_SHO("Saintia Sho"),
  LEGEND("Saint Seiya Legend Of Sanctuary"),
  LOST_CANVAS("The Lost Canvas"),
  THE_BEGINNING("Saint Seiya The Beginning");
  // @formatter:on

  private final String description;

  Series(final String description) {
    this.description = description;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return description;
  }
}
