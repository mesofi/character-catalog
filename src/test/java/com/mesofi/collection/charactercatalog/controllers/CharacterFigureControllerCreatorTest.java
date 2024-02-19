/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Feb 13, 2024.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;
import java.nio.file.Files;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

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
   * CharacterFigureController#loadAllCharacters(org.springframework.web.multipart.MultipartFile)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void loadAllCharacters_whenFileMissing_thenBadRequest() throws Exception {
    mockMvc
        .perform(multipart(BASE_URL + "/loader"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(
            jsonPath("message").value(containsString("Required part 'file' is not present.")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(0)));
  }

  /**
   * Test for {@link
   * CharacterFigureController#loadAllCharacters(org.springframework.web.multipart.MultipartFile)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void loadAllCharacters_whenFileProvided_thenSuccess() throws Exception {
    when(characterFigureService.loadAllCharacters(any(MultipartFile.class))).thenReturn(4l);

    final String CATALOG = "characters/MythCloth Catalog - CatalogMyth-min.tsv";
    final byte[] bytes = Files.readAllBytes(getPathFromClassPath(CATALOG));

    mockMvc
        .perform(multipart(BASE_URL + "/loader").file("file", bytes))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andExpect(content().string("")); // size of the test input file
  }

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
        .andExpect(jsonPath("message").value(containsString("Validation failed for argument ")))
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
        .andExpect(jsonPath("message").value(containsString("Validation failed for argument ")))
        .andExpect(jsonPath("errors").isArray())
        .andExpect(jsonPath("errors", hasSize(1)))
        .andExpect(jsonPath("errors").value("group: Provide a valid group"));
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
  /**
   * Test for {@link
   * CharacterFigureController#createNewCharacter(com.mesofi.collection.charactercatalog.model.CharacterFigure)}
   *
   * @throws Exception If there's an exception during the call.
   */
  @Test
  public void createNewCharacter_whenBasicInfo_thenCreateCharacterSuccesfully() throws Exception {
    String id = UUID.randomUUID().toString();
    CharacterFigure expectedCharacter = new CharacterFigure();
    expectedCharacter.setId(id);
    expectedCharacter.setBaseName("Whale Moses");

    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Whale Moses");
    characterFigure.setGroup(Group.SILVER);
    when(characterFigureService.createNewCharacter(characterFigure)).thenReturn(expectedCharacter);

    final String jsonRequest =
        """
            {
                "baseName": "Whale Moses",
                "group": "SILVER"
            }""";
    mockMvc
        .perform(post(BASE_URL).contentType(APPLICATION_JSON).content(jsonRequest))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.originalName").doesNotExist())
        .andExpect(jsonPath("$.baseName").value("Whale Moses"))
        .andExpect(jsonPath("$.displayableName").doesNotExist())
        .andExpect(jsonPath("$.lineUp").doesNotExist())
        .andExpect(jsonPath("$.series").doesNotExist())
        .andExpect(jsonPath("$.group").doesNotExist())
        .andExpect(jsonPath("$.metalBody").value(false))
        .andExpect(jsonPath("$.oce").value(false))
        .andExpect(jsonPath("$.revival").value(false))
        .andExpect(jsonPath("$.plainCloth").value(false))
        .andExpect(jsonPath("$.brokenCloth").value(false))
        .andExpect(jsonPath("$.bronzeToGold").value(false))
        .andExpect(jsonPath("$.gold").value(false))
        .andExpect(jsonPath("$.hongKongVersion").value(false))
        .andExpect(jsonPath("$.manga").value(false))
        .andExpect(jsonPath("$.surplice").value(false))
        .andExpect(jsonPath("$.set").value(false))
        .andExpect(jsonPath("$.anniversary").doesNotExist())
        .andExpect(jsonPath("$.tags").doesNotExist())
        .andExpect(jsonPath("$.images").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.basePrice").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.firstAnnouncementDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.preorderDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.preorderConfirmationDay").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.releaseDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceJPY.releaseConfirmationDay").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.basePrice").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.firstAnnouncementDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.preorderDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.preorderConfirmationDay").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.releaseDate").doesNotExist())
        .andExpect(jsonPath("$.issuanceMXN.releaseConfirmationDay").doesNotExist())
        .andExpect(jsonPath("$.futureRelease").value(false))
        .andExpect(jsonPath("$.url").doesNotExist())
        .andExpect(jsonPath("$.distribution").doesNotExist())
        .andExpect(jsonPath("$.remarks").doesNotExist());

    verify(characterFigureService).createNewCharacter(characterFigure);
  }
}
