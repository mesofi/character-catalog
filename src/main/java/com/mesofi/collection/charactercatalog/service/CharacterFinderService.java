/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Oct 10, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the logic to find an existing characters.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class CharacterFinderService {

    private CharacterFigureRepository repository;

    public List<CharacterFigure> findCharacterByName(final String name) {
        log.debug("Finding a character with name: {}", name);
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Provide a non empty figure name.");
        } 

        return null;
    }
}