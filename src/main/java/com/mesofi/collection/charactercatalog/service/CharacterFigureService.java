package com.mesofi.collection.charactercatalog.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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

    public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
        return characterRepository.save(characterFigure);
    }

    public List<CharacterFigure> retrieveAllCharacters(final String name) {
        if (!StringUtils.hasLength(name)) {
            return characterRepository.findAllByOrderByReleaseDate();
        } else {
            String filteredName = name.toLowerCase().trim();
            if (name.length() > 2) {
                filteredName = filterName(config.getKeywordExclude(), null, filteredName);
            }
            retrieveCharacterByName(name);
            findCharacterFigureByName(filteredName);
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
            log.debug("LineUp found: {}", lineUp);
            filteredName = filterName(LineUp.values(), filteredName);

            List<CharacterFigure> allCharactersByLineUp = characterRepository.findAllBylineUp(lineUp);

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
                final int MAX_DISTANCE = 10;
                Entry<Integer, String> firstEntry = matches.firstEntry();
                if (firstEntry.getKey() < MAX_DISTANCE) {
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

    private LineUp findLineUp(String name) {
        return Stream.of(LineUp.values())
                .filter($ -> org.apache.commons.lang3.StringUtils.containsAnyIgnoreCase(name, $.getFriendlyName()))
                .findFirst().orElse(LineUp.MYTH_CLOTH);
    }

    private void findCharacterFigureByName(String filteredName) {
        log.debug("Finding with [{}]", filteredName);

        StringBuilder sb = new StringBuilder();
        // String[] splited = ;

        Arrays.stream(filteredName.split("\\s+")).forEach($ -> {

        });

        LevenshteinDistance distance = new LevenshteinDistance();
        List<CharacterFigure> all = characterRepository.findAll();

        Map<Integer, String> dist = new TreeMap<>();

        for (CharacterFigure characterFigure : all) {
            // System.out.println(characterFigure.getName().toLowerCase());
            // System.out.println(distance.apply(characterFigure.getName().toLowerCase(), filteredName));
            dist.put(distance.apply(characterFigure.getName().toLowerCase(), filteredName),
                    characterFigure.getName().toLowerCase());
        }
        System.out.println(dist);

    }

    private String filterName(LineUp[] lineUps, String name) {
        return filterName(Stream.of(lineUps).map($ -> $.getFriendlyName()).collect(Collectors.toList()), null, name);
    }

    private String filterName(List<String> exclusionList, String symbolExclusion, String name) {
        if (StringUtils.hasLength(symbolExclusion)) {
            name = name.replaceAll(symbolExclusion, "");
        }

        StringBuilder sb = new StringBuilder();
        String[] splited = name.split("\\s+");

        Arrays.stream(splited).filter($ -> !exclusionList.stream().anyMatch($::equalsIgnoreCase)).forEach($ -> {
            sb.append($);
            sb.append(" ");
        });

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
