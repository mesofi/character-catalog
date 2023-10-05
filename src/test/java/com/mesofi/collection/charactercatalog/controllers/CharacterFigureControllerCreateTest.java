/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for
 * {@link CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
 * 
 * @author armandorivasarzaluz
 *
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    private final String BASE_URL = "/characters";

    @Test
    public void should_return_bad_request_when_body_is_missing() throws Exception {
        // @formatter:off
        mockMvc.perform(post(BASE_URL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value(containsString("Required request body is missing")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty())
                ;
        // @formatter:on
    }

    @Test
    public void should_return_bad_request_when_body_is_empty() throws Exception {
        // @formatter:off
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value(containsString("Validation failed for argument")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.length()").value(2))
                .andExpect(jsonPath("$.errors[0]").value("baseName: Provide a non empty base name"))
                .andExpect(jsonPath("$.errors[1]").value("group: Provide a valid group"))
                ;
        // @formatter:on
    }
}
