/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Oct 10, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
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
    private CharacterFigureService characterFigureService;

    public List<CharacterFigure> findCharacterByName(final String name) {
        log.debug("Finding a character with name: '{}'", name);
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Provide a non empty figure name.");
        }
        // @formatter:off
        List<String> exclusions = Stream.of("Bandai", "Saint", "Seiya", "Myth", "Cloth",
                        "Masami", "Kurumada", "Cross", "Correction", "BOX", "Modification", "No", "Japan", "version",
                        "OF", "-", "/")
                .map(String::toLowerCase)
                .toList();
        // @formatter:on

        // @formatter:off
        Set<String> simpleNameKeywords = Arrays.stream(name.split("\\s+"))
                .map(String::toLowerCase)
                .filter($ -> !exclusions.contains($))
                .map(this::removeSpecialCharacters)
                .filter($ -> !exclusions.contains($))
                .collect(Collectors.toSet());
        // @formatter:on

        List<CharacterFigureEntity> list = repository.findAll();
        for (String nameKeyword : simpleNameKeywords) {
            // @formatter:off
            list = list.stream()
                    .filter($ -> Objects.nonNull($.getTags()))
                    .filter($ -> $.getTags().contains(nameKeyword))
                    .toList();
            // @formatter:on
            log.debug("[{}] matches found so far", list.size());
            if (list.size() == 1) {
                // we found at least one match
                break;
            }
        }

        return list.stream().map($ -> characterFigureService.fromEntityToDisplayableFigure($))
                .collect(Collectors.toList());
    }

    private String removeSpecialCharacters(String word) {
        // removes characters for example: [], (), - etc ...
        return word.replaceAll("[\\[\\]()-]", "");
    }
}