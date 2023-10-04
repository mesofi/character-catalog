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
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Figure;
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

        log.debug("Total of figures to be loaded: {}", allCharacters.size());
        List<CharacterFigure> effectiveCharacters = getEffectiveCharacters(allCharacters);
        log.debug("Total of effective figures to be loaded: {}", effectiveCharacters.size());

        // performs a mapping and saves the records in the DB ...
        // @formatter:off
        long total = repository.saveAll(effectiveCharacters.stream()
                        .map($ -> modelMapper.toEntity($))
                        .collect(Collectors.toList())).size();
        // @formatter:on
        log.debug("Total of figures loaded correctly: {}", total);
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
                    existing.setRestocks(addRestock(existing.getRestocks(), curr));
                } else {
                    effectiveCharacters.add(curr);
                }
            }
            return effectiveCharacters;
        }
        return new ArrayList<>();
    }

    /**
     * Retrieve all the characters ordered by release date.
     * 
     * @return The list of characters.
     */
    public List<CharacterFigure> retrieveAllCharacters() {
        // @formatter:off
        List<CharacterFigure> figureList = repository.findAll(getSorting()).stream()
                .map($ -> modelMapper.toModel($))
                .peek(this::calculatePriceAndDisplayableName)
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
    private void calculatePriceAndDisplayableName(final CharacterFigure figure) {
        Issuance jpy = figure.getIssuanceJPY();
        Issuance mxn = figure.getIssuanceMXN();

        // the displayable name is calculated here
        figure.setDisplayableName(calculateFigureDisplayableName(figure));
        // the price is set here.
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
     * @param newCharacter The character to be persisted.
     * @return The saved character.
     */
    public CharacterFigure createNewCharacter(final CharacterFigure newCharacter) {
        log.debug("Creating a brand new character ...");

        // performs some validations.
        validateCharacterFigure(newCharacter);

        // check if the new figure is part of restocking, or it is a new one.
        List<CharacterFigureEntity> existingFigures = repository.findAll(getSorting());
        log.debug("We found {} existing stored figures ...", existingFigures.size());

        // @formatter:off
        Optional<CharacterFigure> characterFound = existingFigures.stream()
                .map($ -> modelMapper.toModel($))
                .filter(newCharacter::equals)
                .findFirst();
        // @formatter:on

        CharacterFigure cf;
        if (characterFound.isPresent()) {
            // the new character can be added as a re-stocking.
            cf = characterFound.get();
            cf.setRestocks(addRestock(cf.getRestocks(), newCharacter));

            // once it's added as re-stocking, then it's updated in our DB.
            repository.findById(cf.getId()).ifPresentOrElse(entity -> {
                entity.setRestocks(addRestock(entity.getRestocks(), newCharacter));
                repository.save(entity); // updates with the new re-stocking
                log.debug("The new character has been added as restock of {}, id: {}", cf.getBaseName(), cf.getId());
            }, () -> log.warn("No restock has been added"));
        } else {
            // the new character is saved for the first time.
            cf = modelMapper.toModel(repository.save(modelMapper.toEntity(newCharacter)));
            log.debug("A new character has been saved with id: {}", cf.getId());
        }
        // finally the price and name is calculated here ...
        calculatePriceAndDisplayableName(cf);
        return cf;
    }

    /**
     * This method is used to validate a character object.
     *
     * @param character The character to be validated.
     */
    private void validateCharacterFigure(final CharacterFigure character) {

        // make sure the required fields are there...
        if (Objects.isNull(character)) {
            throw new IllegalArgumentException("Provide a valid character");
        }
        if (!StringUtils.hasText(character.getBaseName())) {
            throw new IllegalArgumentException(INVALID_BASE_NAME);
        }
        if (Objects.isNull(character.getGroup())) {
            throw new IllegalArgumentException(INVALID_GROUP);
        }

        // now make sure some required fields are defaulted.
        if (!StringUtils.hasText(character.getOriginalName())) {
            character.setOriginalName(character.getBaseName());
        }
        if (Objects.isNull(character.getLineUp())) {
            character.setLineUp(LineUp.MYTH_CLOTH_EX);
        }
        if (Objects.isNull(character.getSeries())) {
            character.setSeries(Series.SAINT_SEIYA);
        }

        if (Objects.isNull(character.getIssuanceJPY())) {
            character.setFutureRelease(true);
        } else {
            character.setFutureRelease(Objects.isNull(character.getIssuanceJPY().getReleaseDate()));
        }
    }

    private Sort getSorting() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "futureRelease"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "issuanceJPY.releaseDate"));
        return Sort.by(orders);
    }

    private StringBuilder replacePattern(String name) {
        return new StringBuilder(name.replaceFirst("~", "(").replaceFirst("~", ")"));
    }

    private void appendAttr(StringBuilder sb, String attribute) {
        sb.append(" ");
        sb.append(attribute);
    }

    /**
     * This method is used to add a figure as re-stock. If the existing list of
     * re-stock is null, then it creates a new one and the figure is added into the
     * list.
     *
     * @param restocks   The list of re-stocks.
     * @param newRestock The new figure to be added as re-stock.
     * @return The new re-stocking list which includes the new item added.
     */
    private List<RestockFigure> addRestock(List<RestockFigure> restocks, final Figure newRestock) {
        if (Objects.isNull(restocks)) {
            restocks = new ArrayList<>();
        }

        RestockFigure newRestockFigure = new RestockFigure();
        newRestockFigure.setIssuanceJPY(newRestock.getIssuanceJPY());
        newRestockFigure.setIssuanceMXN(newRestock.getIssuanceMXN());
        newRestockFigure.setFutureRelease(newRestock.isFutureRelease());
        newRestockFigure.setUrl(newRestock.getUrl());
        newRestockFigure.setDistribution(newRestock.getDistribution());
        newRestockFigure.setRemarks(newRestock.getRemarks());

        restocks.add(newRestockFigure);
        return restocks;
    }
}