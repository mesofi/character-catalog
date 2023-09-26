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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
import com.mesofi.collection.charactercatalog.model.Figure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockFigure;
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

        // @formatter:off
        List<CharacterFigure> allCharacters = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .skip(1) // we don't consider the header
                .map($ -> fileMapper.fromLineToCharacterFigure($))
                .collect(Collectors.toList());
        // @formatter:on

        // reverse the list so that we can add re-stocks easily ...
        reverseCharacters(allCharacters);

        log.debug("Total of figures loaded: {}", allCharacters.size());
        List<CharacterFigure> effectiveCharacters = getEffectiveCharacters(allCharacters);
        log.debug("Total of effective figures to be loaded: {}", effectiveCharacters.size());

        // performs a mapping and saves the records in the DB ...
        // @formatter:off
        long total = characterFigureRepository.saveAll(effectiveCharacters.stream()
                        .map($ -> modelMapper.toEntity($))
                        .collect(Collectors.toList())).size();
        // @formatter:on
        log.debug("Total of figures saved: {}", total);
    }

    private <T> void reverseCharacters(List<T> allCharacters) {
        if (Objects.nonNull(allCharacters) && !allCharacters.isEmpty()) {
            T value = allCharacters.remove(0);

            // call the recursive function to reverse
            // the list after removing the first element
            reverseCharacters(allCharacters);

            // now after the rest of the list has been
            // reversed by the upper recursive call,
            // add the first value at the end
            allCharacters.add(value);
        }
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
        List<CharacterFigure> figureList = characterFigureRepository.findAllByOrderByReleaseDateDesc().stream()
                .map($ -> modelMapper.toModel($))
                .peek($ -> $.setDisplayableName(calculateFigureDisplayableName($)))
                .peek($ -> $.setReleasePrice(calculateReleasePrice($.getBasePrice(), $.getReleaseDate())))
                .peek($ -> {
                    $.setOriginalName(null);
                    $.setBaseName(null);
                })
                .toList();
        // @formatter:on
        log.debug("Total of characters found: {}", figureList.size());
        return figureList;
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
        restockFigure.setBasePrice(restock.getBasePrice());
        restockFigure.setReleasePrice(restock.getReleasePrice());
        restockFigure.setFirstAnnouncementDate(restock.getFirstAnnouncementDate());
        restockFigure.setPreorderDate(restock.getPreorderDate());
        restockFigure.setPreorderConfirmationDay(restock.getPreorderConfirmationDay());
        restockFigure.setReleaseDate(restock.getReleaseDate());
        restockFigure.setReleaseConfirmationDay(restock.getReleaseConfirmationDay());
        restockFigure.setUrl(restock.getUrl());
        restockFigure.setDistribution(restock.getDistribution());
        restockFigure.setRemarks(restock.getRemarks());
        restockList.add(restockFigure);
    }

    private void copyRestock(CharacterFigure restock, CharacterFigure source) {
        List<RestockFigure> restockList = source.getRestocks();
        if (Objects.isNull(restockList)) {
            source.setRestocks(new ArrayList<>());
        }
        // the source is added itself as re-stock.
        List<RestockFigure> restocks = source.getRestocks();
        RestockFigure restockFigure = new RestockFigure();
        copyCommonInfo(source, restockFigure);
        restocks.add(restockFigure);

        // now the base info is updated.
        copyCommonInfo(restock, source);
        source.setOriginalName(restock.getOriginalName());
        source.setBaseName(restock.getBaseName());
        source.setLineUp(restock.getLineUp());
        source.setSeries(restock.getSeries());
        source.setGroup(restock.getGroup());
        source.setMetalBody(restock.isMetalBody());
        source.setOce(restock.isOce());
        source.setRevival(restock.isRevival());
        source.setPlainCloth(restock.isPlainCloth());
        source.setBrokenCloth(restock.isBrokenCloth());
        source.setBronzeToGold(restock.isBronzeToGold());
        source.setGold(restock.isGold());
        source.setHongKongVersion(restock.isHongKongVersion());
        source.setManga(restock.isManga());
        source.setSurplice(restock.isSurplice());
        source.setSet(restock.isSet());
        source.setAnniversary(restock.getAnniversary());
    }

    private void copyCommonInfo(Figure from, Figure target) {
        target.setBasePrice(from.getBasePrice());
        target.setReleasePrice(from.getReleasePrice());
        target.setFirstAnnouncementDate(from.getFirstAnnouncementDate());
        target.setPreorderDate(from.getPreorderDate());
        target.setPreorderConfirmationDay(from.getPreorderConfirmationDay());
        target.setReleaseDate(from.getReleaseDate());
        target.setReleaseConfirmationDay(from.getReleaseConfirmationDay());
        target.setUrl(from.getUrl());
        target.setDistribution(from.getDistribution());
        target.setRemarks(from.getRemarks());
    }

}
