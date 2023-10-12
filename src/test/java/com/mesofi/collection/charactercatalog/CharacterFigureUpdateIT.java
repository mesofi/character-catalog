/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.mesofi.collection.charactercatalog.controllers.CharacterFigureController;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Test the character update process.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureUpdateIT {

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
        log.debug("Creating a single basic character ...");

        // We start by deleting all the existing characters.
        repository.deleteAll();

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("baseName", "Phoenix Ikki")
                .put("group", "V2")
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
                .jsonPath("$.futureRelease").isEqualTo(true)
                .jsonPath("$.url").doesNotExist()
                .jsonPath("$.distribution").doesNotExist()
                .jsonPath("$.remarks").doesNotExist()
                .jsonPath("$.tags").doesNotExist()
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").doesNotExist()
                .jsonPath("$.baseName").doesNotExist()
                .jsonPath("$.displayableName").isEqualTo("Phoenix Ikki ~New Bronze Cloth~")
                .jsonPath("$.lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$.series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$.group").isEqualTo("V2")
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
        log.debug("The character have been created correctly! ...");
    }

    /**
     * {@link CharacterFigureController#getAllCharactersByName(String)}
     * {@link CharacterFigureController#updateExistingCharacter(String, CharacterFigure)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(2)
    void should_update_character_by_id() throws JSONException {
        log.debug("Retrieve all the existing characters");

        // @formatter:off
        List<CharacterFigure> existingCharacters = webTestClient.get()
                .uri(BASE_URL + CONTEXT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CharacterFigure.class)
                .hasSize(1)
                .returnResult()
                .getResponseBody();
        // @formatter:on
        if (Objects.nonNull(existingCharacters) && !existingCharacters.isEmpty()) {
            String id = existingCharacters.get(0).getId();
            assertNotNull(id);
            // we retrieve the character by id.

            JSONObject issuanceJPY = new JSONObject();
            issuanceJPY.put("basePrice", "12.000");
            issuanceJPY.put("releaseDate", LocalDate.of(2024, 2, 1));
            issuanceJPY.put("firstAnnouncementDate", LocalDate.of(2024, 4, 10));
            issuanceJPY.put("preorderDate", LocalDate.of(2023, 4, 26));
            issuanceJPY.put("preorderConfirmationDay", true);
            issuanceJPY.put("releaseDate", LocalDate.of(2024, 9, 16));
            issuanceJPY.put("releaseConfirmationDay", true);

            JSONArray tags = new JSONArray();
            tags.put(0, "ex");
            tags.put(1, "gold");

            // @formatter:off
            String postRequestBody = new JSONObject()
                    .put("baseName", "Scorpio")
                    .put("group", "GOLD")
                    .put("issuanceJPY", issuanceJPY)
                    .put("tags", tags)
                    .toString();
            // @formatter:on

            // @formatter:off
            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + CONTEXT + "/{id}").build(id))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(postRequestBody))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.issuanceJPY.basePrice").isEqualTo(12.0)
                    .jsonPath("$.issuanceJPY.releasePrice").isEqualTo(13.2)
                    .jsonPath("$.issuanceJPY.firstAnnouncementDate").isEqualTo(LocalDate.of(2024, 4, 10).toString())
                    .jsonPath("$.issuanceJPY.preorderDate").isEqualTo(LocalDate.of(2023, 4, 26).toString())
                    .jsonPath("$.issuanceJPY.preorderConfirmationDay").isEqualTo(true)
                    .jsonPath("$.issuanceJPY.releaseDate").isEqualTo(LocalDate.of(2024, 9, 16).toString())
                    .jsonPath("$.issuanceJPY.releaseConfirmationDay").isEqualTo(true)
                    .jsonPath("$.issuanceMXN").doesNotExist()
                    .jsonPath("$.futureRelease").isEqualTo(false)
                    .jsonPath("$.url").doesNotExist()
                    .jsonPath("$.distribution").doesNotExist()
                    .jsonPath("$.remarks").doesNotExist()
                    .jsonPath("$.tags").exists()
                    .jsonPath("$.tags").isArray()
                    .jsonPath("$.tags.length()").isEqualTo(2)
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
        } else {
            // the flow should not end up here.
            fail("Unexpected flow ...");
        }
    }
}
