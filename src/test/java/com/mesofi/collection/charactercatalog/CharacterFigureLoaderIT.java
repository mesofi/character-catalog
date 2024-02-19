/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Feb 7, 2024.
 */
package com.mesofi.collection.charactercatalog;

import com.mesofi.collection.charactercatalog.controllers.CharacterFigureController;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * Test the process of loading characters.
 *
 * @author armandorivasarzaluz
 */
@Slf4j
@ActiveProfiles("itest")
@AutoConfigureWebTestClient(timeout = "60000")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureLoaderIT {

  @Autowired private WebTestClient webTestClient;

  final String BASE_URL = "";
  final String CONTEXT = "/characters";

  /**
   * {@link
   * CharacterFigureController#loadAllCharacters(org.springframework.web.multipart.MultipartFile)}
   */
  @Test
  @Order(1)
  void loadAllCharacters_WhenPartialFile_ThenLoadRecords() {
    log.debug("Loading all the characters for the first time (partial records) ...");

    final String data = "characters/MythCloth Catalog - CatalogMyth-min.tsv";
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder
        .part("file", new ClassPathResource(data))
        .contentType(MediaType.MULTIPART_FORM_DATA);

    // @formatter:off
    webTestClient
        .post()
        .uri(BASE_URL + CONTEXT + "/loader")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .exchange()
        .expectStatus()
        .isAccepted();
    // @formatter:on
    log.debug("The characters have been loaded correctly! ...");
  }

  /**
   * {@link
   * CharacterFigureController#getAllCharacters(com.mesofi.collection.charactercatalog.model.RestockType,
   * String)}
   */
  @Test
  @Order(2)
  void getAllCharacters_WhenPartialFileLoaded_ThenVerifyRecordsLoaded() {
    log.debug("Retrieving all existing characters ...");

    // @formatter:off
    webTestClient
        .get()
        .uri(BASE_URL + CONTEXT)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$")
        .isArray()
        .jsonPath("$.length()")
        .isEqualTo(6)
        .jsonPath("$[0].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[0].remarks")
        .doesNotExist()
        .jsonPath("$[0].tags")
        .value(Matchers.hasItems("ex", "dohko", "libra", "revival"))
        .jsonPath("$[0].displayableName")
        .isEqualTo("Libra Dohko")
        .jsonPath("$[0].lineUp")
        .isEqualTo("MYTH_CLOTH_EX")
        .jsonPath("$[0].series")
        .isEqualTo("SAINT_SEIYA")
        .jsonPath("$[0].group")
        .isEqualTo("GOLD")
        .jsonPath("$[0].metalBody")
        .isEqualTo(false)
        .jsonPath("$[0].oce")
        .isEqualTo(false)
        .jsonPath("$[0].revival")
        .isEqualTo(true)
        .jsonPath("$[0].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[0].brokenCloth")
        .isEqualTo(false)
        .jsonPath("$[0].bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$[0].gold")
        .isEqualTo(false)
        .jsonPath("$[0].hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$[0].manga")
        .isEqualTo(false)
        .jsonPath("$[0].surplice")
        .isEqualTo(false)
        .jsonPath("$[0].set")
        .isEqualTo(false)
        .jsonPath("$[0].anniversary")
        .doesNotExist()
        .jsonPath("$[1].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[1].displayableName")
        .isEqualTo("Pegasus Seiya (Initial Bronze Cloth) ~20th Anniversary Ver.~")
        .jsonPath("$[1].lineUp")
        .isEqualTo("MYTH_CLOTH")
        .jsonPath("$[1].series")
        .isEqualTo("SAINT_SEIYA")
        .jsonPath("$[1].group")
        .isEqualTo("V1")
        .jsonPath("$[1].metalBody")
        .isEqualTo(false)
        .jsonPath("$[1].oce")
        .isEqualTo(false)
        .jsonPath("$[1].revival")
        .isEqualTo(false)
        .jsonPath("$[1].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[1].brokenCloth")
        .isEqualTo(false)
        .jsonPath("$[1].bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$[1].gold")
        .isEqualTo(false)
        .jsonPath("$[1].hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$[1].manga")
        .isEqualTo(false)
        .jsonPath("$[1].surplice")
        .isEqualTo(false)
        .jsonPath("$[1].set")
        .isEqualTo(false)
        .jsonPath("$[2].issuanceJPY.basePrice")
        .isEqualTo(32000)
        .jsonPath("$[2].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[2].displayableName")
        .isEqualTo("Goddess Athena & Saori Kido ~Divine Saga Premium Set~")
        .jsonPath("$[2].lineUp")
        .isEqualTo("MYTH_CLOTH_EX")
        .jsonPath("$[2].series")
        .isEqualTo("SAINT_SEIYA")
        .jsonPath("$[2].group")
        .isEqualTo("GOD")
        .jsonPath("$[2].metalBody")
        .isEqualTo(true)
        .jsonPath("$[2].oce")
        .isEqualTo(false)
        .jsonPath("$[2].revival")
        .isEqualTo(false)
        .jsonPath("$[2].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[2].brokenCloth")
        .isEqualTo(false)
        .jsonPath("$[2].bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$[2].gold")
        .isEqualTo(false)
        .jsonPath("$[2].hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$[2].manga")
        .isEqualTo(false)
        .jsonPath("$[2].surplice")
        .isEqualTo(false)
        .jsonPath("$[2].set")
        .isEqualTo(true)
        .jsonPath("$[3].issuanceJPY.basePrice")
        .isEqualTo(16000)
        .jsonPath("$[3].issuanceJPY.releasePrice")
        .isEqualTo(17280)
        .jsonPath("$[3].issuanceJPY.firstAnnouncementDate")
        .doesNotExist()
        .jsonPath("$[3].issuanceJPY.preorderDate")
        .isEqualTo("2016-05-07")
        .jsonPath("$[3].issuanceJPY.preorderConfirmationDay")
        .isEqualTo(true)
        .jsonPath("$[3].issuanceJPY.releaseDate")
        .isEqualTo("2016-11-26")
        .jsonPath("$[3].issuanceJPY.releaseConfirmationDay")
        .isEqualTo(true)
        .jsonPath("$[3].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[3].url")
        .isEqualTo("https://tamashiiweb.com/item/11447")
        .jsonPath("$[3].distribution")
        .isEqualTo("STORES")
        .jsonPath("$[3].displayableName")
        .isEqualTo("Taurus Aldebaran")
        .jsonPath("$[3].lineUp")
        .isEqualTo("MYTH_CLOTH_EX")
        .jsonPath("$[3].series")
        .isEqualTo("SOG")
        .jsonPath("$[3].group")
        .isEqualTo("GOLD")
        .jsonPath("$[3].metalBody")
        .isEqualTo(false)
        .jsonPath("$[3].oce")
        .isEqualTo(false)
        .jsonPath("$[3].revival")
        .isEqualTo(false)
        .jsonPath("$[3].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[3].brokenCloth")
        .isEqualTo(false)
        .jsonPath("$[3].bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$[3].gold")
        .isEqualTo(false)
        .jsonPath("$[3].hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$[3].manga")
        .isEqualTo(false)
        .jsonPath("$[3].surplice")
        .isEqualTo(false)
        .jsonPath("$[3].set")
        .isEqualTo(false)
        .jsonPath("$[4].issuanceJPY.basePrice")
        .isEqualTo(600)
        .jsonPath("$[4].issuanceJPY.releasePrice")
        .isEqualTo(648)
        .jsonPath("$[4].issuanceJPY.preorderDate")
        .doesNotExist()
        .jsonPath("$[4].issuanceJPY.preorderConfirmationDay")
        .doesNotExist()
        .jsonPath("$[4].issuanceJPY.releaseDate")
        .isEqualTo("2016-08-08")
        .jsonPath("$[4].issuanceJPY.releaseConfirmationDay")
        .isEqualTo(true)
        .jsonPath("$[4].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[4].url")
        .doesNotExist()
        .jsonPath("$[4].distribution")
        .isEqualTo("OTHER")
        .jsonPath("$[4].displayableName")
        .isEqualTo("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~ ~HK Version~")
        .jsonPath("$[4].lineUp")
        .isEqualTo("MYTH_CLOTH_EX")
        .jsonPath("$[4].series")
        .isEqualTo("SAINT_SEIYA")
        .jsonPath("$[4].group")
        .isEqualTo("V2")
        .jsonPath("$[4].metalBody")
        .isEqualTo(false)
        .jsonPath("$[4].oce")
        .isEqualTo(false)
        .jsonPath("$[4].revival")
        .isEqualTo(false)
        .jsonPath("$[4].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[4].brokenCloth")
        .isEqualTo(false)
        .jsonPath("$[4].bronzeToGold")
        .isEqualTo(true)
        .jsonPath("$[4].gold")
        .isEqualTo(false)
        .jsonPath("$[4].hongKongVersion")
        .isEqualTo(true)
        .jsonPath("$[4].manga")
        .isEqualTo(false)
        .jsonPath("$[4].surplice")
        .isEqualTo(false)
        .jsonPath("$[4].set")
        .isEqualTo(false)
        .jsonPath("$[4].restocks")
        .doesNotExist()
        .jsonPath("$[4].restocks.length()")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.basePrice")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.firstAnnouncementDate")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.preorderDate")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.preorderConfirmationDay")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.releaseDate")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].issuanceJPY.releaseConfirmationDay")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].futureRelease")
        .doesNotExist()
        .jsonPath("$[4].restocks[0].distribution")
        .doesNotExist()
        .jsonPath("$[5].issuanceJPY.basePrice")
        .isEqualTo(7500)
        .jsonPath("$[5].issuanceJPY.releasePrice")
        .isEqualTo(8100)
        .jsonPath("$[5].issuanceJPY.preorderDate")
        .doesNotExist()
        .jsonPath("$[5].issuanceJPY.preorderConfirmationDay")
        .doesNotExist()
        .jsonPath("$[5].issuanceJPY.releaseDate")
        .isEqualTo("2011-02-11")
        .jsonPath("$[5].issuanceJPY.releaseConfirmationDay")
        .isEqualTo(true)
        .jsonPath("$[5].futureRelease")
        .isEqualTo(false)
        .jsonPath("$[5].url")
        .doesNotExist()
        .jsonPath("$[5].distribution")
        .isEqualTo("TAMASHII_NATIONS")
        .jsonPath("$[5].displayableName")
        .isEqualTo("Pegasus Seiya & Goddess Athena (Broken) ~Original Color Edition~")
        .jsonPath("$[5].lineUp")
        .isEqualTo("MYTH_CLOTH")
        .jsonPath("$[5].series")
        .isEqualTo("SAINT_SEIYA")
        .jsonPath("$[5].group")
        .isEqualTo("OTHER")
        .jsonPath("$[5].metalBody")
        .isEqualTo(false)
        .jsonPath("$[5].oce")
        .isEqualTo(true)
        .jsonPath("$[5].revival")
        .isEqualTo(false)
        .jsonPath("$[5].plainCloth")
        .isEqualTo(false)
        .jsonPath("$[5].brokenCloth")
        .isEqualTo(true)
        .jsonPath("$[5].bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$[5].gold")
        .isEqualTo(false)
        .jsonPath("$[5].hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$[5].manga")
        .isEqualTo(false)
        .jsonPath("$[5].surplice")
        .isEqualTo(false)
        .jsonPath("$[5].set")
        .isEqualTo(false);
    // @formatter:on
    log.debug("The characters has been validated ...");
  }

  /**
   * {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   */
  @Test
  @Order(3)
  void createNewCharacter_WhenExistingRecords_ThenCreateNew() {
    log.debug("Creating a new character ...");

    String newCharacter =
        """
            {
                "baseName": "Whale Moses",
                "group": "SILVER"
            }""";
    webTestClient
        .post()
        .uri(BASE_URL + CONTEXT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(newCharacter)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.futureRelease")
        .isEqualTo(false)
        .jsonPath("$.baseName")
        .isEqualTo("Whale Moses")
        .jsonPath("$.remarks")
        .doesNotExist()
        .jsonPath("$.tags")
        .value(Matchers.hasItems("whale", "moses"))
        .jsonPath("$.displayableName")
        .isEqualTo("Whale Moses")
        .jsonPath("$.lineUp")
        .doesNotExist()
        .jsonPath("$.series")
        .doesNotExist()
        .jsonPath("$.group")
        .isEqualTo("SILVER")
        .jsonPath("$.metalBody")
        .isEqualTo(false)
        .jsonPath("$.oce")
        .isEqualTo(false)
        .jsonPath("$.revival")
        .isEqualTo(false)
        .jsonPath("$.plainCloth")
        .isEqualTo(false)
        .jsonPath("$.brokenCloth")
        .isEqualTo(false)
        .jsonPath("$.bronzeToGold")
        .isEqualTo(false)
        .jsonPath("$.gold")
        .isEqualTo(false)
        .jsonPath("$.hongKongVersion")
        .isEqualTo(false)
        .jsonPath("$.manga")
        .isEqualTo(false)
        .jsonPath("$.surplice")
        .isEqualTo(false)
        .jsonPath("$.set")
        .isEqualTo(false)
        .jsonPath("$.anniversary")
        .doesNotExist();
    log.debug("A new character has been created.");
  }

  /**
   * {@link
   * CharacterFigureController#loadAllCharacters(org.springframework.web.multipart.MultipartFile)}
   */
  @Test
  @Order(4)
  void loadAllCharacters_WhenCompleteFile_ThenLoadRecords() {
    log.debug("Loading all the characters ...");

    final String data = "characters/MythCloth Catalog - CatalogMyth.tsv";
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder
        .part("file", new ClassPathResource(data))
        .contentType(MediaType.MULTIPART_FORM_DATA);

    // @formatter:off
    webTestClient
        .post()
        .uri(BASE_URL + CONTEXT + "/loader")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .exchange()
        .expectStatus()
        .isAccepted();
    // @formatter:on
    log.debug("The characters have been loaded correctly! ...");
  }
}
