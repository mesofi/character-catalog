/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import java.io.Serial;

/**
 * General exception for the service.
 * 
 * @author armandorivasarzaluz
 *
 */
public class CharacterFigureException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CharacterFigureException(String message) {
        super(message);
    }
}
