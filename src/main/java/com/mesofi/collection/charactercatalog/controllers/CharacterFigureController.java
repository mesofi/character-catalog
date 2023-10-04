/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles HTTP requests.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/characters")
public class CharacterFigureController {

    private final CharacterFigureService service;

    /**
     * Handle all the incoming records.
     * 
     * @param file The records to be uploaded.
     */
    @PostMapping("/loader")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Loading all the character records from the original source ...");
        // calls the actual service ...
        service.loadAllCharacters(file);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Creates a new character.
     *
     * @param characterFigure The character to be created.
     * @return The created character.
     */
    @PostMapping
    public CharacterFigure createNewCharacter(@Valid @RequestBody CharacterFigure characterFigure) {
        log.debug("Creating a new character ...");
        return service.createNewCharacter(characterFigure);
    }

    /**
     * Get all existing characters.
     * 
     * @return The list of characters.
     */
    @GetMapping
    public List<CharacterFigure> getAllCharacters() {
        log.debug("Getting all existing characters ...");
        return service.retrieveAllCharacters();
    }

    /**
     * Get all existing characters.
     *
     * @return The list of characters.
     */
    @GetMapping("/{id}")
    public CharacterFigure retrieveCharactersById(@PathVariable String id) {
        log.debug("Getting the character based on id: {}", id);
        return service.retrieveCharactersById(id);
    }
}