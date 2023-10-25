/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mesofi.collection.charactercatalog.service.CharacterFinderService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for
 * {@link CharacterFigureController#updateExistingCharacter(String, CharacterFigure)}
 * 
 * @author armandorivasarzaluz
 *
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;
    @MockBean
    private CharacterFinderService characterFinderService;

    private final String BASE_URL = "/characters";

    @Test
    public void should_update_existing_character() throws Exception {

        String id = "651e9f3bb850c2238f2dfac2";

        CharacterFigure actual = new CharacterFigure();
        actual.setId("651e9f3bb850c2238f2dfac2");
        actual.setBaseName("Scorpio Milo");
        actual.setOriginalName("Scorpio Milo");
        actual.setGroup(Group.V1);
        actual.setLineUp(LineUp.MYTH_CLOTH_EX);
        actual.setSeries(Series.SAINT_SEIYA);

        when(characterFigureService.updateExistingCharacter(anyString(), any(CharacterFigure.class)))
                .thenReturn(actual);

        // @formatter:off
        String postRequestBody = new JSONObject()
                .put("baseName", "Scorpio Milo")
                .put("group", "V1")
                .toString();
        // @formatter:on

        // @formatter:off
        mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issuanceJPY.basePrice").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.releasePrice").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.firstAnnouncementDate").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.preorderDate").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.preorderConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.releaseDate").doesNotExist())
                .andExpect(jsonPath("$.issuanceJPY.releaseConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$.issuanceMXN").doesNotExist())
                .andExpect(jsonPath("$.futureRelease").value(false))
                .andExpect(jsonPath("$.url").doesNotExist())
                .andExpect(jsonPath("$.distribution").doesNotExist())
                .andExpect(jsonPath("$.id").value("651e9f3bb850c2238f2dfac2"))
                .andExpect(jsonPath("$.originalName").value("Scorpio Milo"))
                .andExpect(jsonPath("$.baseName").value("Scorpio Milo"))
                .andExpect(jsonPath("$.displayableName").doesNotExist())
                .andExpect(jsonPath("$.lineUp").value("MYTH_CLOTH_EX"))
                .andExpect(jsonPath("$.series").value("SAINT_SEIYA"))
                .andExpect(jsonPath("$.group").value("V1"))
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
                .andExpect(jsonPath("$.restocks").doesNotExist())
                ;
        // @formatter:on
    }
}
