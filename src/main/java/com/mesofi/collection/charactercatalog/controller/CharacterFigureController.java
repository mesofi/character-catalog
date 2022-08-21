package com.mesofi.collection.charactercatalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Restock;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class CharacterFigureController {

    private final CharacterFigureService service;

    @PostMapping("/characters")
    public ResponseEntity<CharacterFigure> createNewCharacter(@RequestBody CharacterFigure characterFigure) {
        return new ResponseEntity<>(service.createNewCharacter(characterFigure), HttpStatus.OK);
    }

    @GetMapping("/characters")
    public List<CharacterFigure> retrieveAllCharacters(@RequestParam(required = false) String name) {
        if (StringUtils.hasText(name)) {
            log.debug("Retrieving characters using name: [{}]", name);
        } else {
            log.debug("Retrieving all the existing characters");
        }

        return service.retrieveAllCharacters(name);
    }

    @GetMapping("/characters/{id}")
    public ResponseEntity<CharacterFigure> retrieveCharacterById(@PathVariable String id) {
        return new ResponseEntity<>(service.retrieveCharacterById(id), HttpStatus.OK);
    }

    @PatchMapping("/characters/{id}/restocks")
    public ResponseEntity<CharacterFigure> updateCharacterRestock(@PathVariable String id,
            @RequestBody List<Restock> restock) {
        return new ResponseEntity<>(service.updateCharacterRestock(id, restock), HttpStatus.OK);
    }
}