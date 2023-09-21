/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for
 * {@link CharacterFigureController#handleFileUpload(org.springframework.web.multipart.MultipartFile)}
 * 
 * @author armandorivasarzaluz
 *
 */
@ActiveProfiles("test")
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;
    final String BASE_URL = "/characters/loader";

    @Test
    public void should_ReturnBadRequest_WhenBodyIsMissing() throws Exception {

        final byte[] bytes = Files.readAllBytes(Paths.get("TEST_FILE_URL_HERE"));
        // @formatter:off
        mockMvc.perform(multipart(BASE_URL)
                        .file("file", bytes))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("2037")); // size of the test input file
        // @formatter:on
    }
}
