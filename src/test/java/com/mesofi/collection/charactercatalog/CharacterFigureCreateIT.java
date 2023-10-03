/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.mesofi.collection.charactercatalog.controllers.CharacterFigureController;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Test the process of creation of characters.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureCreateIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CharacterFigureRepository repository;

    final String BASE_URL = "";
    final String CONTEXT = "/characters";

    /**
     * {@link CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(1)
    void should_create_basic_character() throws JSONException {
        log.debug("Creating a basic character ...");

        // We start by deleting all the existing characters.
        repository.deleteAll();

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("baseName", "Scorpio")
                .put("group", "GOLD")
                .toString();
        // @formatter:on

        // @formatter:off
        webTestClient.post()
                .uri(BASE_URL + CONTEXT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(postRequestBody))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.issuanceJPY").doesNotExist()
                .jsonPath("$.issuanceMXN").doesNotExist()
                .jsonPath("$.futureRelease").isEqualTo(false)
                .jsonPath("$.url").doesNotExist()
                .jsonPath("$.distribution").doesNotExist()
                .jsonPath("$.remarks").doesNotExist()
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").doesNotExist()
                .jsonPath("$.baseName").doesNotExist()
                .jsonPath("$.displayableName").isEqualTo("Scorpio")
                .jsonPath("$.lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$.series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$.group").isEqualTo("GOLD")
                .jsonPath("$.metalBody").isEqualTo(false)
                .jsonPath("$.oce").isEqualTo(false)
                .jsonPath("$.revival").isEqualTo(false)
                .jsonPath("$.plainCloth").isEqualTo(false)
                .jsonPath("$.brokenCloth").isEqualTo(false)
                .jsonPath("$.bronzeToGold").isEqualTo(false)
                .jsonPath("$.gold").isEqualTo(false)
                .jsonPath("$.hongKongVersion").isEqualTo(false)
                .jsonPath("$.manga").isEqualTo(false)
                .jsonPath("$.surplice").isEqualTo(false)
                .jsonPath("$.set").isEqualTo(false)
                .jsonPath("$.anniversary").doesNotExist()
                .jsonPath("$.restocks").doesNotExist()
        ;
        // @formatter:on
        log.debug("The characters have been created correctly! ...");
    }
}
