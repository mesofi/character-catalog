/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.exception.CharacterFigureNotFoundException;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for {@link CharacterFigureController#retrieveCharactersById(String)}
 * 
 * @author armandorivasarzaluz
 *
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerReadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    private final String BASE_URL = "/characters";

    @Test
    public void should_return_bad_request_when_id_is_blank() throws Exception {

        final String id = " ";
        when(characterFigureService.retrieveCharactersById(anyString()))
                .thenThrow(new IllegalArgumentException("Provide a non empty id to find a character"));

        // @formatter:off
        mockMvc.perform(get(BASE_URL + "/" + id))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Provide a non empty id to find a character"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty())
                ;
        // @formatter:on
    }

    @Test
    public void should_return_not_found_request_id_is_not_found() throws Exception {

        final String id = "1";
        when(characterFigureService.retrieveCharactersById(anyString()))
                .thenThrow(new CharacterFigureNotFoundException("Character not found with id: " + id));

        // @formatter:off
        mockMvc.perform(get(BASE_URL + "/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Character not found with id: 1"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty())
                ;
        // @formatter:on
    }

    @Test
    public void should_return_character_when_it_is_found() throws Exception {

        CharacterFigure actual = new CharacterFigure();
        final String id = "651e9f3bb850c2238f2dfac2";

        actual.setId(id);
        actual.setBaseName("Scorpio Milo");
        actual.setOriginalName("Scorpio Milo");
        actual.setGroup(Group.V1);
        actual.setLineUp(LineUp.MYTH_CLOTH_EX);
        actual.setSeries(Series.SAINT_SEIYA);

        when(characterFigureService.retrieveCharactersById(anyString())).thenReturn(actual);

        // @formatter:off
        mockMvc.perform(get(BASE_URL + "/" + id))
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
