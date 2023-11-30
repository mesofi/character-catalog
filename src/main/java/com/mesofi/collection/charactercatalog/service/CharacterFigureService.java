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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static final String TAG_EX = "ex";
    public static final String TAG_SOG = "soul,gold,god";
    public static final String TAG_REVIVAL = "revival";
    public static final String TAG_SET = "set";
    public static final String TAG_BROKEN = "broken";
    public static final String TAG_METAL = "metal";
    public static final String TAG_OCE = "oce,original,color";
    public static final String TAG_HK = "asia";
    public static final String TAG_BRONZE_TO_GOLD = "golden";

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
            throw new CharacterFigureException("Unable to read characters from file");
        }

        // the records are read and processed now.
        List<CharacterFigureEntity> listEntities = convertStreamToEntityList(inputStream);

        // first, removes all the records.
        repo.deleteAll();

        // performs a mapping and saves the records in the DB ...
        long total = repo.saveAll(listEntities).size();

        log.debug("Total of figures loaded correctly: {}", total);
        return total;
    }

    /**
     * Converts and process the incoming records and return a list with the
     * characters ready to be saved in a persistence storage.
     *
     * @param inputStream Reference to the records read from a source.
     * @return The list or records.
     */
    public List<CharacterFigureEntity> convertStreamToEntityList(InputStream inputStream) {
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
        List<CharacterFigure> effectiveCharacters = getEffectiveCharacters(allCharacters);
        log.debug("Total of effective figures to be loaded: {}", effectiveCharacters.size());

        // add some tags
        addStandardTags(effectiveCharacters);

        // @formatter:off
        return effectiveCharacters.stream()
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
        // We add some more tags depending on the name of the character.
        for (CharacterFigure figure : effectiveCharacters) {
            String[] nameArr = figure.getBaseName().toLowerCase().split("\\s+");
            if (Objects.isNull(figure.getTags())) {
                figure.setTags(new HashSet<>());
            }
            Set<String> existingTags = figure.getTags();
            existingTags.addAll(Arrays.asList(nameArr));
            figure.setTags(existingTags);
        }

        // Now, the standard tags
        addStandardTagToFigure(effectiveCharacters, $ -> $.getLineUp() == LineUp.MYTH_CLOTH_EX, TAG_EX);
        addStandardTagToFigure(effectiveCharacters, $ -> $.getSeries() == Series.SOG, TAG_SOG);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isRevival, TAG_REVIVAL);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isSet, TAG_SET);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isBrokenCloth, TAG_BROKEN);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isMetalBody, TAG_METAL);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isOce, TAG_OCE);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isHongKongVersion, TAG_HK);
        addStandardTagToFigure(effectiveCharacters, CharacterFigure::isBronzeToGold, TAG_BRONZE_TO_GOLD);
    }

    /**
     * Add some standard tags to the figure.
     *
     * @param characters The list of characters.
     * @param predicate  The actual predicate.
     * @param tagNames   The tag names.
     */
    private void addStandardTagToFigure(List<CharacterFigure> characters, Predicate<CharacterFigure> predicate,
            String tagNames) {
        long total = characters.stream().filter(predicate).peek($ -> {
            if (Objects.isNull($.getTags())) {
                $.setTags(new HashSet<>());
            }
            for (String tag : tagNames.split(",")) {
                $.getTags().add(tag);
            }
        }).count();
        log.debug("{} figures have been updated with new tag: [{}]", total, tagNames);
    }

    /**
     * Gets the effective characters, this list contains the records to be saved in
     * DB.
     *
     * @param allCharacters All the characters.
     * @return The effective characters.
     */
    public List<CharacterFigure> getEffectiveCharacters(final List<CharacterFigure> allCharacters) {
        if (Objects.nonNull(allCharacters)) {
            List<CharacterFigure> effectiveCharacters = new ArrayList<>();
            for (CharacterFigure curr : allCharacters) {
                if (effectiveCharacters.contains(curr)) {
                    // add the current character as re-stock in the final list.
                    // CharacterFigure existing =
                    // effectiveCharacters.get(effectiveCharacters.indexOf(curr));
                    // existing.setRestocks(addRestock(existing.getRestocks(), curr));
                    // existing.setTags(addTags(existing.getTags(), curr.getTags()));
                } else {
                    // we add the current character to the final list.
                    effectiveCharacters.add(curr);
                }
            }
            return effectiveCharacters;
        }
        return new ArrayList<>();
    }

    private Set<String> addTags(Set<String> existingTags, Set<String> newTags) {
        if (Objects.isNull(existingTags) & Objects.isNull(newTags)) {
            return null;
        }
        if (Objects.isNull(existingTags)) {
            return newTags;
        }

        if (Objects.isNull(newTags)) {
            return existingTags;
        }

        Set<String> data = new HashSet<>();
        data.addAll(existingTags);
        data.addAll(newTags);
        return data;
    }
}
