/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the business logic of the service.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class CharacterFigureService {

    private CharacterFigureRepository characterFigureRepository;
    private CharacterFigureModelMapper modelMapper;
    private CharacterFigureFileMapper fileMapper;

    /**
     * Loads all the characters.
     * 
     * @param file The reference to the file with all the records.
     */
    public void loadAllCharacters(final MultipartFile file) {
        log.debug("Loading all the records ...");

        if (Objects.isNull(file)) {
            throw new IllegalArgumentException("The uploaded file is missing...");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new CharacterFigureException("Unable to read characters from file");
        }
        // first, removes all the records.
        characterFigureRepository.deleteAll();

        List<CharacterFigure> effectiveCharacters = new ArrayList<>();
        // @formatter:off
        List<CharacterFigure> allCharacters = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .skip(1) // we don't consider the header
                .map($ -> fileMapper.fromLineToCharacterFigure($))
                .toList();
        // @formatter:on

        log.debug("Total of figures loaded: {}", allCharacters.size());
        for (CharacterFigure curr : allCharacters) {
            if (!effectiveCharacters.contains(curr)) {
                effectiveCharacters.add(curr);
            } else {
                // add this character as re-stock
                CharacterFigure other = effectiveCharacters.get(effectiveCharacters.indexOf(curr));
                log.debug("Found potential restock: [{}]", other.getOriginalName());
            }
        }
        log.debug("Total of effective figures to be loaded: {}", effectiveCharacters.size());

        // performs a mapping and saves the records in the DB ...
        // @formatter:off
        long total = characterFigureRepository.saveAll(effectiveCharacters.stream()
                        .map($ -> modelMapper.toEntity($))
                        .collect(Collectors.toList())).size();
        // @formatter:on
        log.debug("Total of figures saved: {}", total);
    }
}
