/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.CommonUtils.reverseListElements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the business logic of the service.
 * 
 * @author armandorivasarzaluz
 */
@Slf4j
@Service
@AllArgsConstructor
public class CharacterFigureService {

    public static final String INVALID_BASE_NAME = "Provide a non empty base name";
    public static final String INVALID_GROUP = "Provide a valid group";
    public static final String INVALID_ID = "Provide a non empty character id";
    public static final String INVALID_IMAGE_ID = "Provide a non empty image id";
    public static final String INVALID_IMAGE_URL = "Provide a non empty url for the image";
    public static final String INVALID_ORDER_NUMBER = "Provide positive value fo the order";

    public static final String DEFAULT_JPG_EXT = ".jpg";
    public static final String DEFAULT_PNG_EXT = ".png";
    public static final String HOST_IMAGE_SIZE = "320x240";
    public static final String HOST_IMAGE_PREFIX = "https://imagizer.imageshack.com/v2/" + HOST_IMAGE_SIZE + "q70/";
    public static final String NO_IMAGE_URL = HOST_IMAGE_PREFIX + "923/3hbcya.png";

    public static final List<String> TAG_EX = List.of("ex");
    public static final List<String> TAG_SOG = List.of("soul", "gold", "god");
    public static final List<String> TAG_REVIVAL = List.of("revival");
    public static final List<String> TAG_SET = List.of("set");
    public static final List<String> TAG_BROKEN = List.of("broken");
    public static final List<String> TAG_METAL = List.of("metal");
    public static final List<String> TAG_OCE = List.of("oce", "original", "color");
    public static final List<String> TAG_HK = List.of("asia");
    public static final List<String> TAG_BRONZE_TO_GOLD = List.of("golden");

    private CharacterFigureRepository repo;
    private CharacterFigureModelMapper modelMapper;
    private CharacterFigureFileMapper fileMapper;

    /**
     * Loads all the characters.
     *
     * @param file The reference to the file with all the records.
     * @return The total of records loaded.
     */
    public long loadAllCharacters(final MultipartFile file) {
        log.debug("Loading all the records ...");

        if (Objects.isNull(file)) {
            throw new IllegalArgumentException("The uploaded file is missing...");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("Can't read input file", e);
            throw new CharacterFigureException("Unable to read characters from initial input file");
        }

        // the records are read and processed now.
        List<CharacterFigureEntity> listEntities = convertStreamToEntityList(inputStream);

        // first, removes all the records.
        repo.deleteAll();

        // performs a save operation in the DB ...
        long total = repo.saveAll(listEntities).size();

        log.info("Total of figures loaded correctly: {}", total);
        return total;
    }

    /**
     * Converts and process the incoming records and return a list with the
     * characters ready to be saved in a persistence storage.
     *
     * @param inputStream Reference to the records read from a source.
     * @return The list or records.
     */
    public List<CharacterFigureEntity> convertStreamToEntityList(@NonNull InputStream inputStream) {
        // @formatter:off
        List<CharacterFigure> allCharacters = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .skip(1) // we don't consider the header
                .map($ -> fileMapper.fromLineToCharacterFigure($))
                .collect(Collectors.toList());
        // @formatter:on

        // reverse the list so that we can add re-stocks easily ...
        reverseListElements(allCharacters);

        // gets the new characters with restocks
        log.debug("Total of figures to be loaded: {}", allCharacters.size());

        // add some tags
        addStandardTags(allCharacters);

        // @formatter:off
        return allCharacters.stream()
                .map($ -> modelMapper.toEntity($))
                .collect(Collectors.toList());
        // @formatter:on
    }

    /**
     * Add some standard tags to the figures. Normally this method should be called
     * if we want to apply certain tags to a specific set of characters, as opposite
     * to set them directly in the catalog (tags very specific).
     *
     * @param effectiveCharacters The list of characters.
     */
    private void addStandardTags(List<CharacterFigure> effectiveCharacters) {
        for (var figure : effectiveCharacters) {
            // make sure the tags are not null.
            if (Objects.isNull(figure.getTags())) {
                figure.setTags(new HashSet<>());
            }
            Set<String> existingTags = figure.getTags();
            // add tags based on the name ...
            existingTags.addAll(Arrays.asList(figure.getBaseName().toLowerCase().split("\\s+")));
            // add tags based on some attributes ...
            if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
                existingTags.addAll(TAG_EX);
            }
            if (figure.getSeries() == Series.SOG) {
                existingTags.addAll(TAG_SOG);
            }
            if (figure.isRevival()) {
                existingTags.addAll(TAG_REVIVAL);
            }
            if (figure.isSet()) {
                existingTags.addAll(TAG_SET);
            }
            if (figure.isBrokenCloth()) {
                existingTags.addAll(TAG_BROKEN);
            }
            if (figure.isMetalBody()) {
                existingTags.addAll(TAG_METAL);
            }
            if (figure.isOce()) {
                existingTags.addAll(TAG_OCE);
            }
            if (figure.isHongKongVersion()) {
                existingTags.addAll(TAG_HK);
            }
            if (figure.isBronzeToGold()) {
                existingTags.addAll(TAG_BRONZE_TO_GOLD);
            }
            figure.setTags(existingTags);
        }
    }
}
