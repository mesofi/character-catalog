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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureNotFoundException;
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
    public static final String INVALID_ID = "Provide a non empty character id";

    public static final String TAG_EX = "ex";
    public static final String TAG_SOG = "soul,gold,god";
    public static final String TAG_REVIVAL = "revival";
    public static final String TAG_SET = "set";
    public static final String TAG_BROKEN = "broken";
    public static final String TAG_METAL = "metal";
    public static final String TAG_OCE = "oce,original,color";

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

        // the records are read and processed now.
        List<CharacterFigureEntity> listEntities = convertStreamToEntityList(inputStream);

        // first, removes all the records.
        repository.deleteAll();

        // performs a mapping and saves the records in the DB ...
        long total = repository.saveAll(listEntities).size();

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
     * Add some standard tags to the figures.
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
                    CharacterFigure existing = effectiveCharacters.get(effectiveCharacters.indexOf(curr));
                    existing.setRestocks(addRestock(existing.getRestocks(), curr));
                    existing.setTags(addTags(existing.getTags(), curr.getTags()));
                } else {
                    // we add the current character to the final list.
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
                .map(this::fromEntityToDisplayableFigure)
                .toList();
        // @formatter:on
        log.debug("Total of characters found: {}", figureList.size());
        return figureList;
    }

    /**
     * This method converts an entity object to the corresponding model, after that,
     * the price and final name are calculated to be displayed. This is a convenient
     * method to be called from other services.
     * 
     * @param entity The raw entity.
     * @return The figure model with the name and price calculated.
     */
    public CharacterFigure fromEntityToDisplayableFigure(CharacterFigureEntity entity) {
        CharacterFigure cf = modelMapper.toModel(entity);
        calculatePriceAndDisplayableName(cf);
        return cf;
    }

    /**
     * Retrieves a character using its identifier.
     *
     * @param id The unique identifier.
     * @return The character found or exception if it was not found.
     */
    public CharacterFigure retrieveCharactersById(final String id) {
        log.debug("Finding a character by id: {}", id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Provide a non empty id to find a character");
        }

        // @formatter:off
        CharacterFigure figure = modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id)));
        // @formatter:on
        calculatePriceAndDisplayableName(figure);
        return figure;
    }

    /**
     * This method is used to prepare the figure to be displayed in the response.
     *
     * @param figure The character figure to be shown.
     */
    private void calculatePriceAndDisplayableName(final CharacterFigure figure) {
        calculateReleasePricing(figure);
        calculateDisplayableName(figure);
    }

    private void calculateReleasePricing(final Figure figure) {
        Issuance jpy = figure.getIssuanceJPY();
        Issuance mxn = figure.getIssuanceMXN();

        // the price is set here.
        if (Objects.nonNull(jpy)) {
            jpy.setReleasePrice(calculateReleasePrice(jpy.getBasePrice(), jpy.getReleaseDate()));
        }
        if (Objects.nonNull(mxn)) {
            mxn.setReleasePrice(mxn.getBasePrice());
        }
    }

    private void calculateDisplayableName(final CharacterFigure figure) {
        // the displayable name is calculated here
        figure.setDisplayableName(calculateFigureDisplayableName(figure));
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
            cf.setTags(addTags(cf.getTags(), newCharacter.getTags()));

            // once it's added as re-stocking, then it's updated in our DB.
            repository.findById(cf.getId()).ifPresentOrElse(entity -> {
                entity.setRestocks(addRestock(entity.getRestocks(), newCharacter));
                entity.setTags(addTags(entity.getTags(), newCharacter.getTags()));
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
        // make sure the character does not have a 're-stock' structure
        if (Objects.nonNull(character.getRestocks())) {
            throw new IllegalArgumentException("Remove restock reference");
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

    /**
     * Updates the existing character with new data, the character found will be
     * replaced by the new one as long as it exists in the DB.
     * 
     * @param id               Unique identifier of the character.
     * @param updatedCharacter The character that will replace the existing one.
     * @return The updated character.
     */
    public CharacterFigure updateExistingCharacter(final String id, final CharacterFigure updatedCharacter) {
        log.debug("Updating existing character with id: {}", id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(INVALID_ID);
        }

        // performs some validations.
        validateCharacterFigure(updatedCharacter);
        CharacterFigureEntity updatedCharacterEntity = modelMapper.toEntity(updatedCharacter);

        CharacterFigureEntity characterFigureEntity = repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id));

        // update the entity ...
        characterFigureEntity.setOriginalName(updatedCharacterEntity.getOriginalName());
        characterFigureEntity.setMetal(updatedCharacterEntity.isMetal());
        characterFigureEntity.setGolden(updatedCharacterEntity.isGolden());
        characterFigureEntity.setHk(updatedCharacterEntity.isHk());
        characterFigureEntity.setIssuanceJPY(fileMapper.createIssuance(updatedCharacterEntity.getIssuanceJPY()));
        characterFigureEntity.setIssuanceMXN(fileMapper.createIssuance(updatedCharacterEntity.getIssuanceMXN()));
        characterFigureEntity.setFutureRelease(updatedCharacterEntity.isFutureRelease());
        characterFigureEntity.setUrl(updatedCharacterEntity.getUrl());
        characterFigureEntity.setDistribution(updatedCharacterEntity.getDistribution());
        characterFigureEntity.setRemarks(updatedCharacterEntity.getRemarks());
        characterFigureEntity.setOriginalName(updatedCharacterEntity.getOriginalName());
        characterFigureEntity.setBaseName(updatedCharacterEntity.getBaseName());
        characterFigureEntity.setLineUp(updatedCharacterEntity.getLineUp());
        characterFigureEntity.setSeries(updatedCharacterEntity.getSeries());
        characterFigureEntity.setGroup(updatedCharacterEntity.getGroup());
        characterFigureEntity.setOce(updatedCharacterEntity.isOce());
        characterFigureEntity.setRevival(updatedCharacterEntity.isRevival());
        characterFigureEntity.setPlainCloth(updatedCharacterEntity.isPlainCloth());
        characterFigureEntity.setBrokenCloth(updatedCharacterEntity.isBrokenCloth());
        characterFigureEntity.setGold(updatedCharacterEntity.isGold());
        characterFigureEntity.setManga(updatedCharacterEntity.isManga());
        characterFigureEntity.setSurplice(updatedCharacterEntity.isSurplice());
        characterFigureEntity.setSet(updatedCharacterEntity.isSet());
        characterFigureEntity.setAnniversary(updatedCharacterEntity.getAnniversary());
        List<RestockFigure> list = updatedCharacterEntity.getRestocks();
        if (list != null) {
            characterFigureEntity.setRestocks(new ArrayList<>(list));
        }
        Set<String> set = updatedCharacterEntity.getTags();
        if (set != null) {
            characterFigureEntity.setTags(new LinkedHashSet<>(set));
        }

        // the character is updated here.
        repository.save(characterFigureEntity);
        log.debug("Character has been updated correctly!");

        // retrieves the entity directly from the DB so that we can send to the
        // response...
        // @formatter:off
        CharacterFigure figure = modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id)));
        // @formatter:on
        calculatePriceAndDisplayableName(figure);
        return figure;
    }

    /**
     * Update the tags in an existing character.
     * 
     * @param id   The unique identifier for the character.
     * @param tags The list of tags to be added.
     * @return The character updated with new tags.
     */
    public CharacterFigure updateTagsInCharacter(@NonNull final String id, @Nullable final Set<String> tags) {
        log.debug("Updating tags: {} from this character: {}", tags, id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(INVALID_ID);
        }

        CharacterFigureEntity characterFigureEntity = repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id));

        if (Objects.nonNull(characterFigureEntity.getTags()) & Objects.nonNull(tags)) {
            Set<String> hashSet = new HashSet<>();
            hashSet.addAll(characterFigureEntity.getTags());
            hashSet.addAll(tags);
            characterFigureEntity.setTags(hashSet);
        }

        if (Objects.isNull(characterFigureEntity.getTags()) & Objects.nonNull(tags)) {
            characterFigureEntity.setTags(new HashSet<>(tags));
        }

        if (Objects.isNull(characterFigureEntity.getTags()) & Objects.isNull(tags)) {
            // it's not necessary to update anything in the DB since the tags do not exist.
            return modelMapper.toModel(characterFigureEntity);
        }

        // update the tags.
        repository.save(characterFigureEntity);
        log.debug("Tags updated correctly!!!");

        // gets the character updated.
        CharacterFigure figure = modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id)));
        calculatePriceAndDisplayableName(figure);
        return figure;
    }

    /**
     * Deletes all the tags from an existing character.
     * 
     * @param id The unique identifier for the character.
     * @return The updated character.
     */
    public CharacterFigure deleteAllTagsInCharacter(@NonNull final String id) {
        log.debug("Update character {} to delete all the existing tags", id);

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(INVALID_ID);
        }

        CharacterFigureEntity characterFigureEntity = repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id));

        // deletes all the tags
        characterFigureEntity.setTags(null);

        // deletes the tags.
        repository.save(characterFigureEntity);
        log.debug("Tags deleted correctly!!!");

        // gets the character updated.
        CharacterFigure figure = modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new CharacterFigureNotFoundException("Character not found with id: " + id)));
        calculatePriceAndDisplayableName(figure);
        return figure;
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
        calculateReleasePricing(newRestockFigure); // the release price is set here.

        restocks.add(newRestockFigure);
        return restocks;
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