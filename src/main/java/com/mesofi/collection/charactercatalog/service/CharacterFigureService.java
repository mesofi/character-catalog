package com.mesofi.collection.charactercatalog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Restock;
import com.mesofi.collection.charactercatalog.model.Tag;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;
import com.mesofi.collection.charactercatalog.repository.CharacterUpdatableRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CharacterFigureService {

    private final CharacterConfig config;
    private final CharacterRepository characterRepository;
    private final CharacterUpdatableRepository characterUpdatableRepository;

    private final String SPACE = " ";
    private final String SPACE_REGEX = "\\s+";

    public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
        return characterRepository.save(characterFigure);
    }

    public List<CharacterFigure> retrieveAllCharacters(final String name) {
        if (!StringUtils.hasLength(name)) {
            List<CharacterFigure> allCharacters = characterRepository.findAllByOrderByReleaseDate();
            // the price is calculated here.
            Long totalCharacters = allCharacters.stream().filter($ -> Objects.nonNull($.getTax()))
                    .peek($ -> $.setPrice($.getBasePrice().add($.getBasePrice().multiply($.getTax())))).count();

            log.debug("Total characters found: {}", totalCharacters);
            return allCharacters;
        } else {
            String filteredName = name.toLowerCase().trim();
            if (name.length() > 2) {
                filteredName = filterName(config.getKeywordExclude(), null, filteredName);
            }
            retrieveCharacterByName(name);
        }
        return null;
    }

    public Optional<CharacterFigure> retrieveCharacterByName(final String name) {
        log.debug("Finding a character with name: '{}'", name);
        if (!StringUtils.hasLength(name) || name.trim().length() < 2) {
            // throw exception
        } else {
            String filteredName = filterName(config.getKeywordExclude(), config.getSymbolExclude(), name);
            log.debug("After applying filtering: '{}'", filteredName);

            LineUp lineUp = findLineUp(filteredName);
            log.debug("The LineUp associated to this figure is: {}", lineUp);
            filteredName = removeKeywords(filteredName,
                    Stream.of(LineUp.values()).map($ -> $.getFriendlyName()).collect(Collectors.toList()));

            List<CharacterFigure> allCharactersByLineUp = characterRepository.findAllBylineUp(lineUp);
            // once we found all the characters for a given lineUp, we keep looking for tags.
            log.debug("Looking for a match: '{}'", filteredName);
            filterCharactersByTags(filteredName, allCharactersByLineUp, Tag.values());
            log.debug("We found {} characters matching with the tags", allCharactersByLineUp.size());

            TreeMap<Integer, String> matches = new TreeMap<>();
            int minDistance = Integer.MAX_VALUE;
            LevenshteinDistance distance = new LevenshteinDistance();
            for (CharacterFigure characterFigure : allCharactersByLineUp) {
                // calculate the distance ...
                minDistance = distance.apply(characterFigure.getName().toLowerCase(), filteredName.toLowerCase());
                if (matches.containsKey(minDistance)) {
                    log.warn("[{}] and [{}] have the same distance: {}", characterFigure.getName().toLowerCase(),
                            matches.get(minDistance).toLowerCase(), minDistance);
                }

                matches.put(minDistance, characterFigure.getName());
            }

            if (!matches.isEmpty()) {
                final int MAX_DISTANCE = 13;
                Entry<Integer, String> firstEntry = matches.firstEntry();
                if (firstEntry.getKey() <= MAX_DISTANCE) {
                    String matchName = firstEntry.getValue();
                    return allCharactersByLineUp.stream().filter($ -> $.getName().equalsIgnoreCase(matchName))
                            .peek($ -> log.debug("Figure found: '{}'", $.getName())).findFirst();
                } else {
                    log.warn("Distance too far from: {}, actual: {}", MAX_DISTANCE, firstEntry.getKey());
                }
            }
            return Optional.empty();
        }
        return null;
    }

    private void filterCharactersByTags(String name, List<CharacterFigure> allCharacters, Tag[] tags) {
        Set<String> allTags = new HashSet<>();
        for (Tag tag : tags) {
            allTags.add(tag.name());
        }

        List<String> matchedTags = new ArrayList<>();
        String[] allWords = name.split(SPACE_REGEX);
        for (String word : allWords) {
            if (allTags.contains(word.toUpperCase())) {
                matchedTags.add(word.toUpperCase());
            }
        }
    }

    private LineUp findLineUp(String name) {
        return Stream.of(LineUp.values())
                .filter($ -> org.apache.commons.lang3.StringUtils.containsAnyIgnoreCase(name, $.getFriendlyName()))
                .findFirst().orElse(LineUp.MYTH_CLOTH);
    }

    private String filterName(List<String> exclusionList, String symbolExclusion, String name) {
        name = removeSpecialCharacters(name, symbolExclusion);
        name = removeKeywords(name, exclusionList);
        return removeDuplicates(name);
    }

    private String removeSpecialCharacters(String theString, String specialCharacters) {
        if (StringUtils.hasLength(specialCharacters)) {
            // remove special characters by replacing them with an empty string.
            return theString.replaceAll(specialCharacters, "");
        }
        // no modification was performed.
        return theString;
    }

    private String removeKeywords(String theString, List<String> exclusionList) {
        StringBuilder sb = new StringBuilder();
        String[] allWords = theString.split(SPACE_REGEX);

        // exclude those keyword.
        Arrays.stream(allWords).filter($ -> !exclusionList.stream().anyMatch($::equalsIgnoreCase)).forEach($ -> {
            sb.append($);
            sb.append(SPACE);
        });

        return sb.toString().trim();
    }

    private String removeDuplicates(String theString) {
        StringBuilder sb = new StringBuilder();
        String[] allWords = theString.split(SPACE_REGEX);

        // Convert String Array allWords to LinkedHashSet to remove duplicates
        LinkedHashSet<String> set = new LinkedHashSet<String>(Arrays.asList(allWords));
        for (String word : set) {
            sb.append(word);
            sb.append(SPACE);
        }
        return sb.toString().trim();
    }

    public CharacterFigure retrieveCharacterById(String id) {
        return characterRepository.findById(id).get();
    }

    public CharacterFigure updateCharacterRestock(String id, List<Restock> restock) {
        characterUpdatableRepository.updateRestocks(id, restock);
        return characterRepository.findById(id).get();
    }

}
