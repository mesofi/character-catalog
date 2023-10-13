/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.mesofi.collection.charactercatalog.controllers.CharacterFigureController;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Test the process of loading characters.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("itest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureLoaderIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CharacterFigureRepository repository;

    final String BASE_URL = "";
    final String CONTEXT = "/characters";

    /**
     * {@link CharacterFigureController#handleFileUpload(org.springframework.web.multipart.MultipartFile)}
     */
    @Test
    @Order(1)
    void should_load_all_characters_partial_file() {
        log.debug("Loading all the characters for the first time (partial records) ...");

        // We start by deleting all the existing characters.
        repository.deleteAll();

        final String data = "characters/MythCloth Catalog - CatalogMyth-min.tsv";
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource(data)).contentType(MediaType.MULTIPART_FORM_DATA);

        // @formatter:off
        webTestClient.post()
                .uri(BASE_URL + CONTEXT + "/loader")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isAccepted();
        // @formatter:on
        log.debug("The characters have been loaded correctly! ...");
    }

    /**
     * {@link CharacterFigureController#getAllCharactersByName(String)}
     */
    @Test
    @Order(2)
    void should_verify_characters_loaded() {
        log.debug("Retrieving all existing characters ...");

        // @formatter:off
        webTestClient.get()
                .uri(BASE_URL + CONTEXT)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(6)
                .jsonPath("$[0].futureRelease").isEqualTo(true)
                .jsonPath("$[0].remarks").isEqualTo("20th anniversary")
                .jsonPath("$[0].tags").value(Matchers.hasItems("shun","andromeda"))
                .jsonPath("$[0].displayableName").isEqualTo("Andromeda Shun (Initial Bronze Cloth) ~20th Anniversary Ver.~")
                .jsonPath("$[0].lineUp").isEqualTo("MYTH_CLOTH")
                .jsonPath("$[0].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[0].group").isEqualTo("V1")
                .jsonPath("$[0].metalBody").isEqualTo(false)
                .jsonPath("$[0].oce").isEqualTo(false)
                .jsonPath("$[0].revival").isEqualTo(false)
                .jsonPath("$[0].plainCloth").isEqualTo(false)
                .jsonPath("$[0].brokenCloth").isEqualTo(false)
                .jsonPath("$[0].bronzeToGold").isEqualTo(false)
                .jsonPath("$[0].gold").isEqualTo(false)
                .jsonPath("$[0].hongKongVersion").isEqualTo(false)
                .jsonPath("$[0].manga").isEqualTo(false)
                .jsonPath("$[0].surplice").isEqualTo(false)
                .jsonPath("$[0].set").isEqualTo(false)
                .jsonPath("$[0].anniversary").isEqualTo(20)

                .jsonPath("$[1].futureRelease").isEqualTo(true)
                .jsonPath("$[1].displayableName").isEqualTo("Taurus Aldebaran")
                .jsonPath("$[1].lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$[1].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[1].group").isEqualTo("GOLD")
                .jsonPath("$[1].metalBody").isEqualTo(false)
                .jsonPath("$[1].oce").isEqualTo(false)
                .jsonPath("$[1].revival").isEqualTo(true)
                .jsonPath("$[1].plainCloth").isEqualTo(false)
                .jsonPath("$[1].brokenCloth").isEqualTo(false)
                .jsonPath("$[1].bronzeToGold").isEqualTo(false)
                .jsonPath("$[1].gold").isEqualTo(false)
                .jsonPath("$[1].hongKongVersion").isEqualTo(false)
                .jsonPath("$[1].manga").isEqualTo(false)
                .jsonPath("$[1].surplice").isEqualTo(false)
                .jsonPath("$[1].set").isEqualTo(false)

                .jsonPath("$[2].issuanceJPY.basePrice").isEqualTo(0)
                .jsonPath("$[2].futureRelease").isEqualTo(true)
                .jsonPath("$[2].displayableName").isEqualTo("Epsilon Alioth Fenril")
                .jsonPath("$[2].lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$[2].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[2].group").isEqualTo("ROBE")
                .jsonPath("$[2].metalBody").isEqualTo(false)
                .jsonPath("$[2].oce").isEqualTo(false)
                .jsonPath("$[2].revival").isEqualTo(false)
                .jsonPath("$[2].plainCloth").isEqualTo(false)
                .jsonPath("$[2].brokenCloth").isEqualTo(false)
                .jsonPath("$[2].bronzeToGold").isEqualTo(false)
                .jsonPath("$[2].gold").isEqualTo(false)
                .jsonPath("$[2].hongKongVersion").isEqualTo(false)
                .jsonPath("$[2].manga").isEqualTo(false)
                .jsonPath("$[2].surplice").isEqualTo(false)
                .jsonPath("$[2].set").isEqualTo(false)

                .jsonPath("$[3].issuanceJPY.basePrice").isEqualTo(22.000)
                .jsonPath("$[3].issuanceJPY.releasePrice").isEqualTo(24.20000)
                .jsonPath("$[3].issuanceJPY.firstAnnouncementDate").isEqualTo("2023-08-30")
                .jsonPath("$[3].issuanceJPY.preorderDate").isEqualTo("2023-09-01")
                .jsonPath("$[3].issuanceJPY.preorderConfirmationDay").isEqualTo(true)
                .jsonPath("$[3].issuanceJPY.releaseDate").isEqualTo("2024-02-01")
                .jsonPath("$[3].issuanceJPY.releaseConfirmationDay").isEqualTo(false)
                .jsonPath("$[3].futureRelease").isEqualTo(false)
                .jsonPath("$[3].url").isEqualTo("https://tamashiiweb.com/item/14590")
                .jsonPath("$[3].distribution").isEqualTo("STORES")
                .jsonPath("$[3].displayableName").isEqualTo("Libra Dohko")
                .jsonPath("$[3].lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$[3].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[3].group").isEqualTo("GOLD")
                .jsonPath("$[3].metalBody").isEqualTo(false)
                .jsonPath("$[3].oce").isEqualTo(false)
                .jsonPath("$[3].revival").isEqualTo(true)
                .jsonPath("$[3].plainCloth").isEqualTo(false)
                .jsonPath("$[3].brokenCloth").isEqualTo(false)
                .jsonPath("$[3].bronzeToGold").isEqualTo(false)
                .jsonPath("$[3].gold").isEqualTo(false)
                .jsonPath("$[3].hongKongVersion").isEqualTo(false)
                .jsonPath("$[3].manga").isEqualTo(false)
                .jsonPath("$[3].surplice").isEqualTo(false)
                .jsonPath("$[3].set").isEqualTo(false)

                .jsonPath("$[4].issuanceJPY.basePrice").isEqualTo(9.200)
                .jsonPath("$[4].issuanceJPY.releasePrice").isEqualTo(9.93600)
                .jsonPath("$[4].issuanceJPY.preorderDate").isEqualTo("2017-06-06")
                .jsonPath("$[4].issuanceJPY.preorderConfirmationDay").isEqualTo(true)
                .jsonPath("$[4].issuanceJPY.releaseDate").isEqualTo("2017-09-23")
                .jsonPath("$[4].issuanceJPY.releaseConfirmationDay").isEqualTo(true)
                .jsonPath("$[4].futureRelease").isEqualTo(false)
                .jsonPath("$[4].url").isEqualTo("https://tamashiiweb.com/item/12197")
                .jsonPath("$[4].distribution").isEqualTo("STORES")
                .jsonPath("$[4].displayableName").isEqualTo("Virgo Shaka")
                .jsonPath("$[4].lineUp").isEqualTo("MYTH_CLOTH_EX")
                .jsonPath("$[4].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[4].group").isEqualTo("GOLD")
                .jsonPath("$[4].metalBody").isEqualTo(false)
                .jsonPath("$[4].oce").isEqualTo(false)
                .jsonPath("$[4].revival").isEqualTo(true)
                .jsonPath("$[4].plainCloth").isEqualTo(false)
                .jsonPath("$[4].brokenCloth").isEqualTo(false)
                .jsonPath("$[4].bronzeToGold").isEqualTo(false)
                .jsonPath("$[4].gold").isEqualTo(false)
                .jsonPath("$[4].hongKongVersion").isEqualTo(false)
                .jsonPath("$[4].manga").isEqualTo(false)
                .jsonPath("$[4].surplice").isEqualTo(false)
                .jsonPath("$[4].set").isEqualTo(false)
                .jsonPath("$[4].restocks").isArray()
                .jsonPath("$[4].restocks.length()").isEqualTo(1)
                .jsonPath("$[4].restocks[0].issuanceJPY.basePrice").isEqualTo(0)
                .jsonPath("$[4].restocks[0].issuanceJPY.firstAnnouncementDate").isEqualTo("2023-09-15")
                .jsonPath("$[4].restocks[0].issuanceJPY.preorderDate").isEqualTo("2023-11-01")
                .jsonPath("$[4].restocks[0].issuanceJPY.preorderConfirmationDay").isEqualTo(false)
                .jsonPath("$[4].restocks[0].issuanceJPY.releaseDate").isEqualTo("2024-06-01")
                .jsonPath("$[4].restocks[0].issuanceJPY.releaseConfirmationDay").isEqualTo(false)
                .jsonPath("$[4].restocks[0].futureRelease").isEqualTo(false)
                .jsonPath("$[4].restocks[0].distribution").isEqualTo("TAMASHII_WEB_SHOP")

                .jsonPath("$[5].issuanceJPY.basePrice").isEqualTo(6.300)
                .jsonPath("$[5].issuanceJPY.releasePrice").isEqualTo(6.80400)
                .jsonPath("$[5].issuanceJPY.preorderDate").isEqualTo("2017-04-03")
                .jsonPath("$[5].issuanceJPY.preorderConfirmationDay").isEqualTo(true)
                .jsonPath("$[5].issuanceJPY.releaseDate").isEqualTo("2017-08-10")
                .jsonPath("$[5].issuanceJPY.releaseConfirmationDay").isEqualTo(true)
                .jsonPath("$[5].futureRelease").isEqualTo(false)
                .jsonPath("$[5].url").isEqualTo("https://tamashiiweb.com/item/11790")
                .jsonPath("$[5].distribution").isEqualTo("STORES")
                .jsonPath("$[5].displayableName").isEqualTo("Pisces Aphrodite")
                .jsonPath("$[5].lineUp").isEqualTo("DDP")
                .jsonPath("$[5].series").isEqualTo("SAINT_SEIYA")
                .jsonPath("$[5].group").isEqualTo("GOLD")
                .jsonPath("$[5].metalBody").isEqualTo(false)
                .jsonPath("$[5].oce").isEqualTo(false)
                .jsonPath("$[5].revival").isEqualTo(false)
                .jsonPath("$[5].plainCloth").isEqualTo(false)
                .jsonPath("$[5].brokenCloth").isEqualTo(false)
                .jsonPath("$[5].bronzeToGold").isEqualTo(false)
                .jsonPath("$[5].gold").isEqualTo(false)
                .jsonPath("$[5].hongKongVersion").isEqualTo(false)
                .jsonPath("$[5].manga").isEqualTo(false)
                .jsonPath("$[5].surplice").isEqualTo(false)
                .jsonPath("$[5].set").isEqualTo(false)
        ;
        // @formatter:on
        log.debug("The characters has been validated ...");
    }

    /**
     * {@link CharacterFigureController#handleFileUpload(org.springframework.web.multipart.MultipartFile)}
     */
    @Test
    @Order(3)
    void should_load_all_characters_complete_file() {
        log.debug("Loading all the characters ...");

        // We start by deleting all the existing characters.
        repository.deleteAll();

        final String data = "characters/MythCloth Catalog - CatalogMyth.tsv";
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource(data)).contentType(MediaType.MULTIPART_FORM_DATA);

        // @formatter:off
        webTestClient.post()
                .uri(BASE_URL + CONTEXT + "/loader")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isAccepted();
        // @formatter:on
        log.debug("The characters have been loaded correctly! ...");
    }
}
