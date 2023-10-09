/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Oct 9, 2023.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for
 * {@link CharacterFigureController#updateTagsInCharacter(String, java.util.Set)}
 * 
 * @author armandorivasarzaluz
 *
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerUpdateTagsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    private final String BASE_URL = "/characters";

    @Test
    public void should_fails_update_tags_when_method_not_supported1() throws Exception {
        // @formatter:off
        mockMvc.perform(patch(BASE_URL))
                .andDo(print())
                .andExpect(status().is(405))
        ;
        // @formatter:on
    }

    @Test
    public void should_fails_update_tags_when_method_not_supported2() throws Exception {
        String id = "65215079a5d1a04590202d71";
        String url = BASE_URL + "/{id}";
        // @formatter:off
        mockMvc.perform(patch(url, id))
                .andDo(print())
                .andExpect(status().is(405))
        ;
        // @formatter:on
    }

    @Test
    public void should_fails_update_tags_when_not_found() throws Exception {
        String id = "65215079a5d1a04590202d71";
        String url = BASE_URL + "/{id}/";
        // @formatter:off
        mockMvc.perform(patch(url, id))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
        // @formatter:on
    }

    @Test
    public void should_update_tags_when_not_tags_are_provided() throws Exception {
        String id = "65215079a5d1a04590202d71";

        CharacterFigure actual = new CharacterFigure();
        actual.setId(id);
        actual.setBaseName("Scorpio Milo");
        actual.setOriginalName("Scorpio Milo");
        actual.setGroup(Group.V1);
        actual.setLineUp(LineUp.MYTH_CLOTH_EX);
        actual.setSeries(Series.SAINT_SEIYA);
        actual.setTags(null);

        // no tags are provided
        when(characterFigureService.updateTagsInCharacter(id, null)).thenReturn(actual);

        String url = BASE_URL + "/{id}/tags";
        // @formatter:off
        mockMvc.perform(patch(url, id))
                .andDo(print())
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
                .andExpect(jsonPath("$.id").value("65215079a5d1a04590202d71"))
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
                .andExpect(jsonPath("$.tags").doesNotExist())
                ;
        // @formatter:on
    }

    @Test
    public void should_update_tags_when_tags_are_provided() throws Exception {
        String id = "65215079a5d1a04590202d71";

        CharacterFigure actual = new CharacterFigure();
        actual.setId(id);
        actual.setBaseName("Scorpio Milo");
        actual.setOriginalName("Scorpio Milo");
        actual.setGroup(Group.V1);
        actual.setLineUp(LineUp.MYTH_CLOTH_EX);
        actual.setSeries(Series.SAINT_SEIYA);
        actual.setTags(Set.of("milo", "ex", "gold"));

        // no tags are provided
        when(characterFigureService.updateTagsInCharacter(id, Set.of("milo", "ex", "gold"))).thenReturn(actual);

        String url = BASE_URL + "/{id}/tags?tags=milo,ex,gold";
        // @formatter:off
        mockMvc.perform(patch(url, id))
                .andDo(print())
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
                .andExpect(jsonPath("$.id").value("65215079a5d1a04590202d71"))
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
                .andExpect(jsonPath("$.tags").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.length()").value(3))
                ;
        // @formatter:on
    }
}