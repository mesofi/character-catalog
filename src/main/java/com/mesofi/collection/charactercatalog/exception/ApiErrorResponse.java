/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 28, 2023.
 */
package com.mesofi.collection.charactercatalog.exception;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

/**
 * The actual API error response.
 * 
 * @author armandorivasarzaluz
 */
@Getter
@Setter
public class ApiErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private Set<String> errors = new TreeSet<>();
}
