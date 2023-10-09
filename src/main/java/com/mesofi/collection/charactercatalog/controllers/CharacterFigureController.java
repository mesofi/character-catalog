/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * Update an existing character.
     * 
     * @param id              The unique identifier.
     * @param characterFigure The new character to be updated.
     * @return The updated character.
     */
    @PutMapping("/{id}")
    public CharacterFigure updateExistingCharacter(@PathVariable String id,
            @Valid @RequestBody CharacterFigure characterFigure) {
        log.debug("Updating existing character with: {}", id);
        return service.updateExistingCharacter(id, characterFigure);
    }

    /**
     * Update the tags in a existing character.
     * 
     * @param id   The unique identifier.
     * @param tags List of tags to be updated.
     * @return The updated character.
     */
    @PatchMapping("/{id}/tags")
    public CharacterFigure updateTagsInCharacter(@PathVariable String id,
            @RequestParam(required = false) Set<String> tags) {
        log.debug("Updating existing character with: {}, and tags: {}", id, tags);
        return service.updateTagsInCharacter(id, tags);
    }

    /**
     * Deletes all the existing characters.
     * 
     * @param id The unique identifier.
     * @return The character updated without tags.
     */
    @DeleteMapping("/{id}/tags")
    public CharacterFigure deleteAllTagsInCharacter(@PathVariable String id) {
        log.debug("Deleting all the existing tags for character: {}", id);
        return service.deleteAllTagsInCharacter(id);
    }
}