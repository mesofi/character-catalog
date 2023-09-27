/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.CommonUtils.reverseListElements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockFigure;
import com.mesofi.collection.charactercatalog.model.Series;
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

    public static final String INVALID_BASE_NAME = "Provide a non empty base name";
    public static final String INVALID_GROUP = "Provide a valid group";

    private CharacterFigureRepository repository;
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
        // first, removes all the records.
        repository.deleteAll();

        // @formatter:off
        List<CharacterFigure> allCharacters = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .skip(1) // we don't consider the header
                .map($ -> fileMapper.fromLineToCharacterFigure($))
                .collect(Collectors.toList());
        // @formatter:on

        // reverse the list so that we can add re-stocks easily ...
        reverseListElements(allCharacters);

        log.debug("Total of figures loaded: {}", allCharacters.size());
        List<CharacterFigure> effectiveCharacters = getEffectiveCharacters(allCharacters);
        log.debug("Total of effective figures to be loaded: {}", effectiveCharacters.size());

        // performs a mapping and saves the records in the DB ...
        // @formatter:off
        long total = repository.saveAll(effectiveCharacters.stream()
                        .map($ -> modelMapper.toEntity($))
                        .collect(Collectors.toList())).size();
        // @formatter:on
        log.debug("Total of figures loaded: {}", total);
        return total;
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
                    // add the current character as re-stock.
                    CharacterFigure existing = effectiveCharacters.get(effectiveCharacters.indexOf(curr));
                    log.debug("Found potential restock: [{}]", curr.getOriginalName());
                    addRestock(existing, curr);
                } else {
                    effectiveCharacters.add(curr);
                }
            }
            return effectiveCharacters;
        }
        return new ArrayList<>();
    }

    /**
     * Retrieve all the characters.
     * 
     * @return The list of characters.
     */
    public List<CharacterFigure> retrieveAllCharacters() {
        // @formatter:off
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "futureRelease"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "issuanceJPY.releaseDate"));

        List<CharacterFigure> figureList = repository.findAll(Sort.by(orders)).stream()
                .map($ -> modelMapper.toModel($))
                .peek(this::prepareCharacterFigure)
                .toList();
        // @formatter:on
        log.debug("Total of characters found: {}", figureList.size());
        return figureList;
    }

    /**
     * This method is used to prepare the figure to be displayed in the response.
     * 
     * @param figure The character figure to be shown.
     */
    private void prepareCharacterFigure(CharacterFigure figure) {
        Issuance jpy = figure.getIssuanceJPY();
        Issuance mxn = figure.getIssuanceMXN();

        figure.setDisplayableName(calculateFigureDisplayableName(figure));
        if (Objects.nonNull(jpy)) {
            jpy.setReleasePrice(calculateReleasePrice(jpy.getBasePrice(), jpy.getReleaseDate()));
        }
        if (Objects.nonNull(mxn)) {
            mxn.setReleasePrice(mxn.getBasePrice());
        }

        figure.setOriginalName(null);
        figure.setBaseName(null);
    }

    private BigDecimal calculateReleasePrice(final BigDecimal basePrice, final LocalDate releaseDate) {
        BigDecimal releasePrice;
        if (Objects.nonNull(basePrice) && Objects.nonNull(releaseDate)) {
            LocalDate october12019 = LocalDate.of(2019, 10, 1);
            if (releaseDate.isBefore(october12019)) {
                releasePrice = basePrice.add(basePrice.multiply(new BigDecimal(".08")));
            } else {
                releasePrice = basePrice.add(basePrice.multiply(new BigDecimal(".10")));
            }
            return releasePrice;
        }
        return null;
    }

    /**
     * Calculate the figure name.
     * 
     * @param figure The figure name object.
     * @return The displayable name.
     */
    public String calculateFigureDisplayableName(final CharacterFigure figure) {
        StringBuilder sb = new StringBuilder();
        sb.append(figure.getBaseName());

        switch (figure.getGroup()) {
        case V1:
            appendAttr(sb, "~Initial Bronze Cloth~");
            break;
        case V2:
            if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
                appendAttr(sb, "~New Bronze Cloth~");
            }
            break;
        case V3:
            appendAttr(sb, "~Final Bronze Cloth~");
            break;
        case V4:
            appendAttr(sb, "(God Cloth)");
            break;
        case V5:
            appendAttr(sb, "(Heaven Chapter)");
            break;
        default:
            break;
        }
        if (figure.isPlainCloth()) {
            appendAttr(sb, "(Plain Clothes)");
        }

        if (figure.isBronzeToGold()) {
            if (figure.getLineUp() == LineUp.MYTH_CLOTH) {
                if (figure.getGroup() == Group.V1) {
                    sb = replacePattern(sb.toString());
                    appendAttr(sb, "~Limited Gold~");
                }
                if (figure.getGroup() == Group.V2) {
                    appendAttr(sb, "~Power of Gold~");
                }
            }
            if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
                if (figure.getGroup() == Group.V2 || figure.getGroup() == Group.V3) {
                    sb = replacePattern(sb.toString());
                    appendAttr(sb, "~Golden Limited Edition~");
                }
            }
        }

        if (figure.isManga()) {
            appendAttr(sb, "~Comic Version~");
        }
        if (figure.isOce()) {
            sb = replacePattern(sb.toString());
            appendAttr(sb, "~Original Color Edition~");
        }
        if (Objects.nonNull(figure.getAnniversary())) {
            sb = replacePattern(sb.toString());
            appendAttr(sb, "~" + figure.getAnniversary() + "th Anniversary Ver.~");
        }
        if (figure.isHongKongVersion()) {
            appendAttr(sb, "~HK Version~");
        }

        if (figure.isSurplice()) {
            appendAttr(sb, "(Surplice)");
        }

        return sb.toString();
    }

    /**
     * Creates a new character.
     * 
     * @param characterFigure The character to be persisted.
     * @return The saved character.
     */
    public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
        log.debug("Creating a new character ...");
        if (Objects.isNull(characterFigure)) {
            throw new IllegalArgumentException("Provide a valid character");
        }
        if (!StringUtils.hasText(characterFigure.getBaseName())) {
            throw new IllegalArgumentException(INVALID_BASE_NAME);
        }
        if (Objects.isNull(characterFigure.getGroup())) {
            throw new IllegalArgumentException(INVALID_GROUP);
        }

        if (!StringUtils.hasText(characterFigure.getOriginalName())) {
            characterFigure.setOriginalName(characterFigure.getBaseName());
        }

        if (Objects.isNull(characterFigure.getLineUp())) {
            characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        }

        if (Objects.isNull(characterFigure.getSeries())) {
            characterFigure.setSeries(Series.SAINT_SEIYA);
        }

        // the character is persisted.
        CharacterFigure figureSaved = modelMapper.toModel(repository.save(modelMapper.toEntity(characterFigure)));
        prepareCharacterFigure(figureSaved);
        log.debug("A new character has been saved with id: {}", characterFigure.getId());
        return figureSaved;
    }

    private StringBuilder replacePattern(String name) {
        return new StringBuilder(name.replaceFirst("~", "(").replaceFirst("~", ")"));
    }

    private void appendAttr(StringBuilder sb, String attribute) {
        sb.append(" ");
        sb.append(attribute);
    }

    private void addRestock(CharacterFigure current, CharacterFigure restock) {
        if (Objects.isNull(current.getRestocks())) {
            current.setRestocks(new ArrayList<>());
        }
        List<RestockFigure> restockList = current.getRestocks();
        RestockFigure restockFigure = new RestockFigure();
        restockFigure.setIssuanceJPY(restock.getIssuanceJPY());
        restockFigure.setIssuanceMXN(restock.getIssuanceMXN());
        restockFigure.setUrl(restock.getUrl());
        restockFigure.setDistribution(restock.getDistribution());
        restockFigure.setRemarks(restock.getRemarks());
        restockList.add(restockFigure);
    }
}