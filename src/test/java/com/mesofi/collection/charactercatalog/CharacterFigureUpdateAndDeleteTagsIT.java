/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Oct 9, 2023.
 */
package com.mesofi.collection.charactercatalog;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
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
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Test the character update and delete tags process.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureUpdateAndDeleteTagsIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CharacterFigureRepository repository;

    final String BASE_URL = "";
    final String CONTEXT = "/characters";

    @BeforeEach
    public void beforeEach() {
        webTestClient = webTestClient.mutate().responseTimeout(Duration.ofMinutes(5)).build();
    }

    /**
     * {@link CharacterFigureController#createNewCharacter(CharacterFigure)}. <br/>
     * Creates a basic character without tags yet.
     */
    @Test
    @Order(1)
    void should_create_basic_character() throws JSONException {
        log.debug("Creating a single basic character without tags ...");

        // We start by deleting all the existing characters.
        repository.deleteAll();

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("baseName", "Phoenix Ikki")
                .put("group", "V2")
                .toString();
        // @formatter:on

        String url = BASE_URL + CONTEXT;
        // @formatter:off
        webTestClient.post()
                .uri(url)
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
                .jsonPath("$.tags").doesNotExist() // No tags created yet.
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
        log.debug("The basic character have been created correctly! ...");
    }

    /**
     * {@link CharacterFigureController#getAllCharactersByName(String)}
     * {@link CharacterFigureController#updateTagsInCharacter(String, java.util.Set)}
     * {@link CharacterFigureController#deleteAllTagsInCharacter(String)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(2)
    void should_update_character_by_id() throws JSONException {
        log.debug("Retrieve all the existing characters");

        String url = BASE_URL + CONTEXT;
        // @formatter:off
        List<CharacterFigure> existingCharacters = webTestClient.get()
                .uri(url)
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

            // add new tags for the first time ...
            // @formatter:off
            webTestClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + CONTEXT + "/{id}/tags")
                            .queryParam("tags", "ex,ikki,myth")
                            .build(id))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.issuanceJPY.basePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.releasePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.firstAnnouncementDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderConfirmationDay").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseConfirmationDay").doesNotExist()
                    .jsonPath("$.issuanceMXN").doesNotExist()
                    .jsonPath("$.futureRelease").isEqualTo(true)
                    .jsonPath("$.url").doesNotExist()
                    .jsonPath("$.distribution").doesNotExist()
                    .jsonPath("$.remarks").doesNotExist()
                    .jsonPath("$.tags").exists()
                    .jsonPath("$.tags").isArray()
                    .jsonPath("$.tags.length()").isEqualTo(3)
                    .jsonPath("$.tags").value(Matchers.hasItems("ex", "ikki", "myth"))
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

            // add new tags for the second time ...
            // @formatter:off
            webTestClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + CONTEXT + "/{id}/tags")
                            .queryParam("tags", "v2, revival")
                            .build(id))
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.issuanceJPY.basePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.releasePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.firstAnnouncementDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderConfirmationDay").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseConfirmationDay").doesNotExist()
                    .jsonPath("$.issuanceMXN").doesNotExist()
                    .jsonPath("$.futureRelease").isEqualTo(true)
                    .jsonPath("$.url").doesNotExist()
                    .jsonPath("$.distribution").doesNotExist()
                    .jsonPath("$.remarks").doesNotExist()
                    .jsonPath("$.tags").exists()
                    .jsonPath("$.tags").isArray()
                    .jsonPath("$.tags.length()").isEqualTo(5)
                    .jsonPath("$.tags").value(Matchers.hasItems("v2", "ex", "revival", "ikki", "myth"))
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

            // remove existing tags ...
            // @formatter:off
            webTestClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL + CONTEXT + "/{id}/tags")
                            .build(id))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("$.issuanceJPY.basePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.releasePrice").doesNotExist()
                    .jsonPath("$.issuanceJPY.firstAnnouncementDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.preorderConfirmationDay").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseDate").doesNotExist()
                    .jsonPath("$.issuanceJPY.releaseConfirmationDay").doesNotExist()
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
        } else {
            // the flow should not end up here.
            fail("Unexpected flow ...");
        }
    }
}
