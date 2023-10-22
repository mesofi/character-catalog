/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import java.time.Duration;
import java.time.LocalDate;

import org.json.JSONArray;
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

    @BeforeEach
    public void beforeEach() {
        webTestClient = webTestClient.mutate().responseTimeout(Duration.ofMinutes(5)).build();
    }

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
                .jsonPath("$.futureRelease").isEqualTo(true)
                .jsonPath("$.url").doesNotExist()
                .jsonPath("$.distribution").doesNotExist()
                .jsonPath("$.remarks").doesNotExist()
                .jsonPath("$.tags").doesNotExist()
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").isEqualTo("Scorpio")
                .jsonPath("$.baseName").isEqualTo("Scorpio")
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
        log.debug("The character have been created correctly! ...");
    }

    /**
     * {@link CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(2)
    void should_create_basic_character_with_restock() throws JSONException {
        log.debug("Creating a basic character with restock ...");

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
                .jsonPath("$.futureRelease").isEqualTo(true)
                .jsonPath("$.url").doesNotExist()
                .jsonPath("$.distribution").doesNotExist()
                .jsonPath("$.remarks").doesNotExist()
                .jsonPath("$.tags").doesNotExist()
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").isEqualTo("Scorpio")
                .jsonPath("$.baseName").isEqualTo("Scorpio")
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
                .jsonPath("$.restocks").exists()
                .jsonPath("$.restocks").isArray()
                .jsonPath("$.restocks.length()").isEqualTo(1)
                .jsonPath("$.restocks[0].issuanceJPY.basePrice").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releasePrice").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.firstAnnouncementDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.preorderDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.preorderConfirmationDay").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releaseDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releaseConfirmationDay").doesNotExist()
                .jsonPath("$.restocks[0].issuanceMX").doesNotExist()
                .jsonPath("$.restocks[0].futureRelease").isEqualTo(true)
                .jsonPath("$.restocks[0].url").doesNotExist()
                .jsonPath("$.restocks[0].distribution").doesNotExist()
                .jsonPath("$.restocks[0].remarks").doesNotExist()
                .jsonPath("$.restocks[0].tags").doesNotExist()
        ;
        // @formatter:on
        log.debug("The character have been created correctly! ...");
    }

    /**
     * {@link CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(3)
    void should_create_basic_character_with_restock_and_tags() throws JSONException {
        log.debug("Creating a basic character with restock and tags ...");

        JSONArray tags = new JSONArray();
        tags.put(0, "ex");
        tags.put(1, "gold");

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("baseName", "Scorpio")
                .put("group", "GOLD")
                .put("tags", tags)
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
                .jsonPath("$.tags").exists()
                .jsonPath("$.tags").isArray()
                .jsonPath("$.tags.length()").isEqualTo(2)
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").isEqualTo("Scorpio")
                .jsonPath("$.baseName").isEqualTo("Scorpio")
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
                .jsonPath("$.restocks").exists()
                .jsonPath("$.restocks").isArray()
                .jsonPath("$.restocks.length()").isEqualTo(2)
                .jsonPath("$.restocks[0].issuanceJPY.basePrice").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releasePrice").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.firstAnnouncementDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.preorderDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.preorderConfirmationDay").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releaseDate").doesNotExist()
                .jsonPath("$.restocks[0].issuanceJPY.releaseConfirmationDay").doesNotExist()
                .jsonPath("$.restocks[0].issuanceMX").doesNotExist()
                .jsonPath("$.restocks[0].futureRelease").isEqualTo(true)
                .jsonPath("$.restocks[0].url").doesNotExist()
                .jsonPath("$.restocks[0].distribution").doesNotExist()
                .jsonPath("$.restocks[0].remarks").doesNotExist()
                .jsonPath("$.restocks[0].tags").doesNotExist()
        ;
        // @formatter:on
        log.debug("The character have been created correctly! ...");
    }

    /**
     * {@link CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
     * 
     * @throws JSONException If there's an exception creating the request body.
     */
    @Test
    @Order(4)
    void should_create_complete_character() throws JSONException {
        log.debug("Creating complete character...");

        JSONObject issuanceJPY = new JSONObject();
        issuanceJPY.put("basePrice", "12.500");
        issuanceJPY.put("releaseDate", LocalDate.of(2024, 2, 1));
        issuanceJPY.put("firstAnnouncementDate", LocalDate.of(2023, 9, 14));
        issuanceJPY.put("preorderDate", LocalDate.of(2023, 9, 15));
        issuanceJPY.put("preorderConfirmationDay", true);
        issuanceJPY.put("releaseDate", LocalDate.of(2024, 2, 1));
        issuanceJPY.put("releaseConfirmationDay", false);

        JSONArray tags = new JSONArray();
        tags.put(0, "ex");
        tags.put(1, "gold");
        tags.put(2, "dragon");

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("originalName", "Dragon Shiryu ~Initial Bronze Cloth 20th Anniversary Ver.~")
                .put("baseName", "Dragon Shiryu")
                .put("lineUp", "MYTH_CLOTH")
                .put("series", "SAINT_SEIYA")
                .put("group", "V1")
                .put("issuanceJPY", issuanceJPY) // issuanceMXN is the same as issuanceJPY
                .put("url", "https://tamashiiweb.com/item/14642")
                .put("distribution", "TAMASHII_WEB_SHOP")
                .put("metalBody", "false")
                .put("oce", "false")
                .put("revival", "false")
                .put("plainCloth", "false")
                .put("brokenCloth", "false")
                .put("bronzeToGold", "false")
                .put("gold", "false")
                .put("hongKongVersion", "false")
                .put("manga", "false")
                .put("surplice", "false")
                .put("set", "false")
                .put("anniversary", "20")
                .put("remarks", "20th anniversary")
                .put("tags", tags)
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
                .jsonPath("$.issuanceJPY.basePrice").isEqualTo(12.5)
                .jsonPath("$.issuanceJPY.releasePrice").isEqualTo(13.75)
                .jsonPath("$.issuanceJPY.firstAnnouncementDate").isEqualTo(LocalDate.of(2023, 9, 14).toString())
                .jsonPath("$.issuanceJPY.preorderDate").isEqualTo(LocalDate.of(2023, 9, 15).toString())
                .jsonPath("$.issuanceJPY.preorderConfirmationDay").isEqualTo(true)
                .jsonPath("$.issuanceJPY.releaseDate").isEqualTo(LocalDate.of(2024, 2, 1).toString())
                .jsonPath("$.issuanceJPY.releaseConfirmationDay").isEqualTo(false)
                .jsonPath("$.issuanceMXN").doesNotExist()
                .jsonPath("$.futureRelease").isEqualTo(false)
                .jsonPath("$.url").isEqualTo("https://tamashiiweb.com/item/14642")
                .jsonPath("$.distribution").isEqualTo("TAMASHII_WEB_SHOP")
                .jsonPath("$.remarks").isEqualTo("20th anniversary")
                .jsonPath("$.tags").exists()
                .jsonPath("$.tags").isArray()
                .jsonPath("$.tags.length()").isEqualTo(3)
                .jsonPath("$.id").exists()
                .jsonPath("$.originalName").isEqualTo("Dragon Shiryu ~Initial Bronze Cloth 20th Anniversary Ver.~")
                .jsonPath("$.baseName").isEqualTo("Dragon Shiryu")
                .jsonPath("$.displayableName").isEqualTo("Dragon Shiryu (Initial Bronze Cloth) ~20th Anniversary Ver.~")
                .jsonPath("$.lineUp").isEqualTo("MYTH_CLOTH")
                .jsonPath("$.series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$.group").isEqualTo("V1")
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
                .jsonPath("$.anniversary").isEqualTo(20)
                .jsonPath("$.restocks").doesNotExist()
        ;
        // @formatter:on
        log.debug("The character have been created correctly! ...");
    }
}
