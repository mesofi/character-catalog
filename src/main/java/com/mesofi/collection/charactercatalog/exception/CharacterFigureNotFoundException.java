/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import java.io.Serial;

/**
 * Exception thrown when the character was not found.
 *
 * @author armandorivasarzaluz
 */
public class CharacterFigureNotFoundException extends CharacterFigureException {

  @Serial private static final long serialVersionUID = 1L;

  public CharacterFigureNotFoundException(String message) {
    super(message);
  }
}
