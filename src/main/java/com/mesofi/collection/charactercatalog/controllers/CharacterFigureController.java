/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 28, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles HTTP requests.
 * 
 * @author armandorivasarzaluz
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/characters")
public class CharacterFigureController {

    private final CharacterFigureService characterFigureService;

    /**
     * Handle all the incoming records.
     * 
     * @param file The records to be uploaded.
     */
    @PostMapping("/loader")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.debug("Loading all the character records from the original source ...");
        // calls the actual service ...
        long total = characterFigureService.loadAllCharacters(file);
        log.debug("Total records loaded: {}", total);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
