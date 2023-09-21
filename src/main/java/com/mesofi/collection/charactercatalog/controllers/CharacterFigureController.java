/**
 * 
 */
package com.mesofi.collection.charactercatalog.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.CharacterFigureResponse;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RestController
@AllArgsConstructor
public class CharacterFigureController {

    private final CharacterFigureService service;
    

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        service.loadAllRecords(file);
    }

    @PostMapping("/characters")
    public ResponseEntity<CharacterFigure> createNewCharacter(
            @Valid @RequestBody final CharacterFigure characterFigure) {

        log.debug("Creating a new character using: {}", characterFigure);
        return new ResponseEntity<>(service.createNewCharacter(characterFigure), HttpStatus.OK);
    }

    @GetMapping("/")
    public List<CharacterFigureResponse> retrieveAllCharacters() {
        
        return service.getAll();
    }
}
