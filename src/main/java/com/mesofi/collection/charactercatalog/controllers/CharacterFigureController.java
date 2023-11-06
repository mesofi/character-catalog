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
import org.springframework.util.StringUtils;
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
import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;
import com.mesofi.collection.charactercatalog.service.CharacterFinderService;

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

    private final CharacterFigureService characterFigureService;
    private final CharacterFinderService characterFinderService;

    /**
     * Handle all the incoming records.
     * 
     * @param file The records to be uploaded.
     */
    @PostMapping("/loader")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Loading all the character records from the original source ...");
        // calls the actual service ...
        characterFigureService.loadAllCharacters(file);
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
        return characterFigureService.createNewCharacter(characterFigure);
    }

    /**
     * Get all existing characters by name, when the name is not provided, then all
     * the characters are retrieved.
     * 
     * @param name The optional name of the character, if this is NOT provided, then
     *             returns all the existing characters.
     * @return The list of characters found based on the name or return all if no
     *         name is provided.
     */
    @GetMapping
    public List<CharacterFigure> getAllCharactersByName(final @RequestParam(required = false) String name) {
        log.debug("Getting all existing characters by name {} ...", name);
        return StringUtils.hasText(name) ? characterFinderService.findCharacterByName(name)
                : characterFigureService.retrieveAllCharacters();
    }

    /**
     * Get all existing characters.
     *
     * @return The list of characters.
     */
    @GetMapping("/{id}")
    public CharacterFigure retrieveCharactersById(@PathVariable String id) {
        log.debug("Getting the character based on id: {}", id);
        return characterFigureService.retrieveCharactersById(id);
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
        return characterFigureService.updateExistingCharacter(id, characterFigure);
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
        return characterFigureService.updateTagsInCharacter(id, tags);
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
        return characterFigureService.deleteAllTagsInCharacter(id);
    }

    /**
     * Adds a new image to an existing character.
     * 
     * @param id           The character unique identifier.
     * @param galleryImage The image to be added.
     * @return The updated character with the new image.
     */
    @PostMapping("/{id}/images")
    public CharacterFigure addImage(@PathVariable String id, @Valid @RequestBody GalleryImage galleryImage) {
        log.debug("Creating a new image associated to this character: {} ...", id);
        return characterFigureService.addImage(id, galleryImage);
    }

    /**
     * Deletes an existing image from a character.
     * 
     * @param id      The unique identifier.
     * @param idImage The identifier of the image.
     * @return The updated character with the new image.
     */
    @DeleteMapping("/{id}/images/{idImage}")
    public CharacterFigure deleteImage(@PathVariable String id, @PathVariable String idImage) {
        log.debug("Deleting existing image from character: {}", id);
        return characterFigureService.deleteImage(id, idImage);
    }

    /**
     * Deletes all existing images from a character.
     * 
     * @param id The unique identifier.
     * @return The updated character with the new image.
     */
    @DeleteMapping("/{id}/images")
    public CharacterFigure deleteAllImages(@PathVariable String id) {
        log.debug("Deleting all existing images from character: {}", id);
        return characterFigureService.deleteImages(id);
    }
}