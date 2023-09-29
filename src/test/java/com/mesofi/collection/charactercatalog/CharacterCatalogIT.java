/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
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

import lombok.extern.slf4j.Slf4j;

/**
 * IT for the service.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterCatalogIT {

    @Autowired
    private WebTestClient webTestClient;

    final String BASE_URL = "/characters";

    @BeforeEach
    public void beforeEach() {
        webTestClient = webTestClient.mutate().responseTimeout(Duration.ofMinutes(5)).build();
    }

    @Test
    @Order(1)
    public void should_upload_source_data() {
        log.debug("===> Uploading a source file ...");

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        String file = "characters/MythCloth Catalog - CatalogMyth-min.tsv";
        bodyBuilder.part("file", new ClassPathResource(file)).contentType(MediaType.MULTIPART_FORM_DATA);

        final String URL = BASE_URL + "/loader";

        // @formatter:off
        webTestClient.post()
                .uri(URL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus()
                .isAccepted()
                ;
        // @formatter:on
    }
}
