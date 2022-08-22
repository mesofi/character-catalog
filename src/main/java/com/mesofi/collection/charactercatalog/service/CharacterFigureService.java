package com.mesofi.collection.charactercatalog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.exceptions.NoSuchCharacterFoundException;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.DistanceFigure;
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

    private static final Map<Integer, Integer> MAX_DISTANCES = new TreeMap<>();
    private static final List<String> LINE_UPS;
    static {
        // max length -> distance
        MAX_DISTANCES.put(10, 13);
        MAX_DISTANCES.put(30, 22);
        MAX_DISTANCES.put(45, 35);
        MAX_DISTANCES.put(60, 42);
        MAX_DISTANCES.put(80, 55);

        LINE_UPS = Stream.of(LineUp.values()).map(LineUp::getFriendlyName).collect(Collectors.toList());
    }

    public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
        if (Objects.isNull(characterFigure)) {
            throw new IllegalArgumentException("Unable to create a new Character");
        }

        return characterRepository.save(characterFigure);
    }

    public List<CharacterFigure> retrieveAllCharacters(final String name) {
        List<CharacterFigure> allCharacters;
        if (StringUtils.hasLength(name)) {
            // tries the best to find the character by its name.
            allCharacters = new ArrayList<>();
            allCharacters.add(retrieveCharacterByName(name)
                    .orElseThrow(() -> new NoSuchCharacterFoundException("Character not found by name: " + name)));
        } else {
            allCharacters = characterRepository.findAllByOrderByReleaseDate();
        }
        // the price is calculated here.
        Long totalCharacters = allCharacters.stream().filter($ -> Objects.nonNull($.getTax()))
                .peek($ -> $.setPrice($.getBasePrice().add($.getBasePrice().multiply($.getTax())))).count();

        log.debug("Total characters found: {}", totalCharacters);
        return allCharacters;

        /*
         * for (CharacterFigure cf : allCharacters) {
         * 
         * cf.setTax(new BigDecimal("0.08")); characterRepository.save(cf); }
         */

        /*
         * Calendar calendar = Calendar.getInstance(); calendar.set(Calendar.YEAR, 2014); calendar.set(Calendar.MONTH,
         * 0); calendar.set(Calendar.DAY_OF_MONTH, 1);
         * 
         * System.out.println(calendar.getTime());
         * 
         * for (CharacterFigure cf : allCharacters) { if(cf.getReleaseDate().before(calendar.getTime())) { cf.setTax(new
         * BigDecimal("0.05")); characterRepository.save(cf); } }
         */
        /*
         * Calendar calendar = Calendar.getInstance(); calendar.set(Calendar.YEAR, 2019); calendar.set(Calendar.MONTH,
         * 0); calendar.set(Calendar.DAY_OF_MONTH, 1);
         * 
         * System.out.println(calendar.getTime());
         * 
         * for (CharacterFigure cf : allCharacters) { if(cf.getReleaseDate().compareTo(calendar.getTime())>=0) {
         * cf.setTax(new BigDecimal("0.10")); characterRepository.save(cf); } }
         */
    }

    public Optional<CharacterFigure> retrieveCharacterByName(final String name) {
        log.debug("Finding a character with name: '{}'", name);
        if (!StringUtils.hasLength(name) || name.trim().length() < 2) {
            // throw exception
            throw new IllegalArgumentException("Provide a valid name");
        } else {
            // Starts with the first exclusion part.
            String filteredName = filterName(config.getKeywordExclude(), config.getSymbolExclude(), name);
            log.debug("After applying filtering exclusion: '{}'", filteredName);

            // finds the corresponding lineUp for the figure.
            LineUp lineUp = findLineUp(filteredName);
            log.debug("The LineUp associated to this figure is: '{}'", lineUp.name());

            // removes the lineUp if it exists in the name.
            filteredName = removeKeywords(filteredName, LINE_UPS);
            log.debug("After applying line - Up exclusion: '{}'", filteredName);

            // finds all the characters for a given line Up.
            List<CharacterFigure> allCharactersByLineUp = characterRepository.findAllBylineUp(lineUp);
            log.debug("We found {} characters for a lineUp: {}", allCharactersByLineUp.size(), lineUp.name());

            // once we found all the characters for a given lineUp, we keep looking for tags.
            filterCharactersByTags(filteredName, allCharactersByLineUp);
            log.debug("We found {} characters matching the tags", allCharactersByLineUp.size());

            // finally, we calculate the distances for the characters.
            List<DistanceFigure> matches = calculateLevenshteinDistance(filteredName, allCharactersByLineUp);
            if (!matches.isEmpty()) {
                DistanceFigure firstEntry = matches.get(0);

                final int MAX_DISTANCE = findMaxDistance(firstEntry.getName().length());
                if (matches.get(0).getDistance() <= MAX_DISTANCE) {
                    String matchName = firstEntry.getName();
                    return allCharactersByLineUp.stream().filter($ -> $.getName().equalsIgnoreCase(matchName)).peek(
                            $ -> log.debug("=> Figure found: '{}' => [{}]", $.getName(), firstEntry.getDistance()))
                            .findFirst();
                } else {
                    log.warn("Distance too far from: {}, actual: {}", MAX_DISTANCE, firstEntry.getDistance());
                    log.warn("[{}] is too different from [{}]", filteredName, firstEntry.getName());
                }
            }
        }
        return Optional.empty();
    }

    private List<DistanceFigure> calculateLevenshteinDistance(String name, List<CharacterFigure> allCharacters) {
        List<DistanceFigure> distanceList = new ArrayList<>();

        LevenshteinDistance distance = new LevenshteinDistance();
        allCharacters.forEach($ -> distanceList
                .add(new DistanceFigure(distance.apply(name.toLowerCase(), $.getName().toLowerCase()), $.getName())));

        return distanceList.stream()
                .sorted(Comparator.comparing(DistanceFigure::getDistance).thenComparing(DistanceFigure::getName))
                .collect(Collectors.toList());
    }

    private int findMaxDistance(final int characterLength) {
        for (Map.Entry<Integer, Integer> entry : MAX_DISTANCES.entrySet()) {
            if (characterLength <= entry.getKey()) {
                return entry.getValue();
            }
        }

        log.warn("** The length is too long [{}] **", characterLength);
        return 0;
    }

    private void filterCharactersByTags(String name, List<CharacterFigure> allCharacters) {
        String[] allWords = name.split(SPACE_REGEX);
        filterCharacters(allWords, 0, allCharacters);
    }

    private void filterCharacters(String[] allWords, int i, List<CharacterFigure> allCharacters) {
        if (i < allWords.length) {
            String currentWord = allWords[i].toUpperCase();

            // finds all the distinct tags for the characters
            // @formatter:off
            List<String> allDistinctTags = allCharacters.stream().filter($ -> Objects.nonNull($.getTags()))
                    .flatMap($ -> $.getTags().stream()).distinct().map(Enum::toString).collect(Collectors.toList());
            // @formatter:on

            if (allDistinctTags.contains(currentWord)) {
                Tag inTag = Tag.valueOf(currentWord);

                // finds all the characters matching the same word and delete those who do not.
                Predicate<CharacterFigure> p = $ -> Objects.nonNull($.getTags()) && $.getTags().contains(inTag);
                allCharacters.removeIf(p.negate());
            }
            filterCharacters(allWords, ++i, allCharacters);
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
        Arrays.stream(allWords).filter($ -> exclusionList.stream().noneMatch($::equalsIgnoreCase)).forEach($ -> {
            sb.append($);
            sb.append(SPACE);
        });

        return sb.toString().trim();
    }

    private String removeDuplicates(String theString) {
        StringBuilder sb = new StringBuilder();
        String[] allWords = theString.split(SPACE_REGEX);

        // Convert String Array allWords to LinkedHashSet to remove duplicates
        LinkedHashSet<String> set = new LinkedHashSet<>(Arrays.asList(allWords));
        for (String word : set) {
            sb.append(word);
            sb.append(SPACE);
        }
        return sb.toString().trim();
    }

    public CharacterFigure retrieveCharacterById(final String id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new NoSuchCharacterFoundException("Character not found: " + id));
    }

    public CharacterFigure updateCharacterRestock(final String id, List<Restock> restock) {
        characterUpdatableRepository.updateRestocks(id, restock);
        return characterRepository.findById(id)
                .orElseThrow(() -> new NoSuchCharacterFoundException("Character not found: " + id));
    }
}
