/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import lombok.extern.slf4j.Slf4j;

/**
 * Just loads the Application Context.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterFigureLoaderIT {

    @Autowired
    private WebTestClient webTestClient;

    final String BASE_URL = "";

    @Test
    void should_load_all_characters() {
        log.debug("Loading all the characters for the first time ...");

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("characters/MythCloth Catalog - CatalogMyth-min.tsv"))
                .contentType(MediaType.MULTIPART_FORM_DATA);

        System.out.println(multipartBodyBuilder);

        webTestClient.post().uri(BASE_URL + "/characters/loader").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange().expectStatus().isAccepted();
        //.expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
          //      .jsonPath("$.englishMessage").doesNotExist().jsonPath("$.frenchMessage").doesNotExist()
            //    .jsonPath("$.transactionId").isEqualTo("WS-40-0024810769").jsonPath("$.status").isEqualTo("completed")
              //  .jsonPath("$.errorDescription").isEqualTo("");

    }
}
