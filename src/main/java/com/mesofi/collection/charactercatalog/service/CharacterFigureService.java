package com.mesofi.collection.charactercatalog.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.config.CharacterConfig;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
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
                filteredName = filterName(config.getKeywordExclude(), filteredName);
            }
            findCharacterFigureByName(filteredName);
        }
        return null;
    }

    private void findCharacterFigureByName(String filteredName) {
        log.debug("Finding with [{}]", filteredName);
    }

    private String filterName(List<String> keywordExclude, String name) {
        StringBuilder sb = new StringBuilder();
        String[] splited = name.split("\\s+");

        Arrays.stream(splited).filter($ -> !keywordExclude.stream().anyMatch($::equalsIgnoreCase)).forEach($ -> {
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
