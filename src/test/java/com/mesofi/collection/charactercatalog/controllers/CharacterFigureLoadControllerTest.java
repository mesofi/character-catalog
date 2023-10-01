/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for
 * {@link CharacterFigureController#handleFileUpload(org.springframework.web.multipart.MultipartFile)}
 * 
 * @author armandorivasarzaluz
 *
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureLoadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    private final String BASE_URL = "/characters/loader";

    @Test
    public void should_return_bad_request_when_file_is_missing() throws Exception {
        // @formatter:off
        mockMvc.perform(multipart(BASE_URL))
                .andDo(print())
                .andExpect(status().isBadRequest());
        // @formatter:on
    }

    @Test
    public void should_return_success_when_file_is_provided() throws Exception {
        final String CATALOG = "characters/MythCloth Catalog - CatalogMyth-min.tsv";
        final byte[] bytes = Files.readAllBytes(getPathFromClassPath(CATALOG));

        // @formatter:off
        mockMvc.perform(multipart(BASE_URL)
                        .file("file", bytes))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string("")); // size of the test input file
        // @formatter:on
    }
}
