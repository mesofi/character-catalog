/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Feb 13, 2024.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.collection.charactercatalog.service.CharacterFigureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test used to create characters.
 *
 * @author armandorivasarzaluz
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerCreatorTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CharacterFigureService characterFigureService;

  private final String BASE_URL = "/characters";

  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenNoRequestBody_thenBadRequest() throws Exception {

    mockMvc
        .perform(post(BASE_URL))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("message").value(containsString("Required request body is missing")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(0)));
  }

  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenNoMediaType_thenUnsupportedMediaType() throws Exception {

    mockMvc
        .perform(post(BASE_URL).content("{}"))
        .andDo(print())
        .andExpect(status().isUnsupportedMediaType())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(
            jsonPath("message").value("Content-Type 'application/octet-stream' is not supported"))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(0)));
  }

  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenEmptyRequestBody_thenBadRequest() throws Exception {

    mockMvc
        .perform(post(BASE_URL).contentType(APPLICATION_JSON).content("{}"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("message").value(containsString("Validation failed for argument [0]")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(2)))
        .andExpect(jsonPath("errors[0]").value("baseName: Provide a non empty base name"))
        .andExpect(jsonPath("errors[1]").value("group: Provide a valid group"));
  }

  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenMissingGroup_thenBadRequest() throws Exception {

    final String jsonRequest =
        """
            {
                "baseName": "Whale Moses"
            }""";
    mockMvc
        .perform(post(BASE_URL).contentType(APPLICATION_JSON).content(jsonRequest))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("message").value(containsString("Validation failed for argument [0]")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(1)))
        .andExpect(jsonPath("errors[0]").value("group: Provide a valid group"));
  }

  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenInvalidGroup_thenBadRequest() throws Exception {

    final String jsonRequest =
        """
            {
                "baseName": "Whale Moses",
                "group": "THE-GROUP"
            }""";
    mockMvc
        .perform(post(BASE_URL).contentType(APPLICATION_JSON).content(jsonRequest))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(
            jsonPath("message")
                .value(
                    containsString(
                        "not one of the values accepted for Enum class: [GOLD, SPECTER, SCALE, STEEL, SILVER, OTHER, SECONDARY, JUDGE, V1, V2, V3, V4, SURPLICE, ROBE, GOD, BLACK, V5]")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(0)));
  }
}
