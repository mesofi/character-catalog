/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Jan 29, 2024.
 */
package com.mesofi.collection.charactercatalog.controllers;

import static com.mesofi.collection.charactercatalog.utils.MockUtils.createBasicMcCharacterFigure;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockType;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

/**
 * Test for {@link CharacterFigureController#getAllCharacters(RestockType)}
 * 
 * @author armandorivasarzaluz
 */
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerFinderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    private final String BASE_URL = "/characters";

    @ParameterizedTest
    @ValueSource(strings = { "invalid-value", "nones" })
    public void should_return_bad_request_when_param_value_is_invalid(final String invalidValue) throws Exception {
        // @formatter:off
        mockMvc.perform(get(BASE_URL).param("restocks", invalidValue))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("'restocks' should be a valid 'RestockType' and '" + invalidValue + "' isn't"));
        // @formatter:on
    }

    @Test
    public void should_return_success_all_records_when_param_value_is_missing() throws Exception {
        List<CharacterFigure> result = new ArrayList<>();
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String id3 = UUID.randomUUID().toString();

        result.add(createBasicMcCharacterFigure(id1, "Aries", new BigDecimal("12000"), LocalDate.of(2023, 2, 2)));
        result.add(createBasicMcCharacterFigure(id2, "Taurus", new BigDecimal("18000"), LocalDate.of(2024, 3, 3)));
        result.add(createBasicMcCharacterFigure(id3, "Gemini", new BigDecimal("15000"), LocalDate.of(2024, 4, 4)));

        when(characterFigureService.retrieveAllCharacters(RestockType.ALL)).thenReturn(result);
        // @formatter:off
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpectAll(allCharacterMatchers(id1, id2, id3))
        ;
        // @formatter:on
    }

    @Test
    public void should_return_success_all_records_when_param_value_is_all_or_not_param() throws Exception {
        List<CharacterFigure> result = new ArrayList<>();
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String id3 = UUID.randomUUID().toString();

        result.add(createBasicMcCharacterFigure(id1, "Aries", new BigDecimal("12000"), LocalDate.of(2023, 2, 2)));
        result.add(createBasicMcCharacterFigure(id2, "Taurus", new BigDecimal("18000"), LocalDate.of(2024, 3, 3)));
        result.add(createBasicMcCharacterFigure(id3, "Gemini", new BigDecimal("15000"), LocalDate.of(2024, 4, 4)));

        when(characterFigureService.retrieveAllCharacters(RestockType.ALL)).thenReturn(result);
        // @formatter:off
        mockMvc.perform(get(BASE_URL)
                        .param("restocks", "ALL"))
                .andDo(print())
                .andExpectAll(allCharacterMatchers(id1, id2, id3));
        // @formatter:on
    }

    private ResultMatcher[] allCharacterMatchers(String... ids) {
        // @formatter:off
        List<ResultMatcher> list = List.of(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$", hasSize(3)),
                jsonPath("$[0].id").value(ids[0]),
                jsonPath("$[0].originalName").value("Aries"),
                jsonPath("$[0].baseName").value("Aries"),
                jsonPath("$[0].displayableName").doesNotExist(),
                jsonPath("$[0].lineUp").value(LineUp.MYTH_CLOTH.name()),
                jsonPath("$[0].series").value(Series.SAINT_SEIYA.name()),
                jsonPath("$[0].group").value(Group.GOLD.name()),
                jsonPath("$[0].metalBody").value(false),
                jsonPath("$[0].oce").value(false),
                jsonPath("$[0].revival").value(false),
                jsonPath("$[0].plainCloth").value(false),
                jsonPath("$[0].brokenCloth").value(false),
                jsonPath("$[0].bronzeToGold").value(false),
                jsonPath("$[0].gold").value(false),
                jsonPath("$[0].hongKongVersion").value(false),
                jsonPath("$[0].manga").value(false),
                jsonPath("$[0].surplice").value(false),
                jsonPath("$[0].set").value(false),
                jsonPath("$[0].anniversary").doesNotExist(),
                jsonPath("$[0].tags").doesNotExist(),
                jsonPath("$[0].images").doesNotExist(),
                jsonPath("$[0].issuanceJPY").exists(),
                jsonPath("$[0].issuanceJPY.basePrice").value("12000"),
                jsonPath("$[0].issuanceJPY.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[0].issuanceJPY.preorderDate").doesNotExist(),
                jsonPath("$[0].issuanceJPY.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[0].issuanceJPY.releaseDate").value("2023-02-02"),
                jsonPath("$[0].issuanceJPY.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[0].issuanceMXN.basePrice").doesNotExist(),
                jsonPath("$[0].issuanceMXN.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[0].issuanceMXN.preorderDate").doesNotExist(),
                jsonPath("$[0].issuanceMXN.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[0].issuanceMXN.releaseDate").doesNotExist(),
                jsonPath("$[0].issuanceMXN.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[0].futureRelease").value(false),
                jsonPath("$[0].url").doesNotExist(),
                jsonPath("$[0].distribution").doesNotExist(),
                jsonPath("$[0].remarks").doesNotExist(),
                jsonPath("$[1].id").value(ids[1]),
                jsonPath("$[1].originalName").value("Taurus"),
                jsonPath("$[1].baseName").value("Taurus"),
                jsonPath("$[1].displayableName").doesNotExist(),
                jsonPath("$[1].lineUp").value(LineUp.MYTH_CLOTH.name()),
                jsonPath("$[1].series").value(Series.SAINT_SEIYA.name()),
                jsonPath("$[1].group").value(Group.GOLD.name()),
                jsonPath("$[1].metalBody").value(false),
                jsonPath("$[1].oce").value(false),
                jsonPath("$[1].revival").value(false),
                jsonPath("$[1].plainCloth").value(false),
                jsonPath("$[1].brokenCloth").value(false),
                jsonPath("$[1].bronzeToGold").value(false),
                jsonPath("$[1].gold").value(false),
                jsonPath("$[1].hongKongVersion").value(false),
                jsonPath("$[1].manga").value(false),
                jsonPath("$[1].surplice").value(false),
                jsonPath("$[1].set").value(false),
                jsonPath("$[1].anniversary").doesNotExist(),
                jsonPath("$[1].tags").doesNotExist(),
                jsonPath("$[1].images").doesNotExist(),
                jsonPath("$[1].issuanceJPY").exists(),
                jsonPath("$[1].issuanceJPY.basePrice").value("18000"),
                jsonPath("$[1].issuanceJPY.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[1].issuanceJPY.preorderDate").doesNotExist(),
                jsonPath("$[1].issuanceJPY.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[1].issuanceJPY.releaseDate").value("2024-03-03"),
                jsonPath("$[1].issuanceJPY.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[1].issuanceMXN.basePrice").doesNotExist(),
                jsonPath("$[1].issuanceMXN.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[1].issuanceMXN.preorderDate").doesNotExist(),
                jsonPath("$[1].issuanceMXN.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[1].issuanceMXN.releaseDate").doesNotExist(),
                jsonPath("$[1].issuanceMXN.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[1].futureRelease").value(false),
                jsonPath("$[1].url").doesNotExist(),
                jsonPath("$[1].distribution").doesNotExist(),
                jsonPath("$[1].remarks").doesNotExist(),
                jsonPath("$[2].id").value(ids[2]),
                jsonPath("$[2].originalName").value("Gemini"),
                jsonPath("$[2].baseName").value("Gemini"),
                jsonPath("$[2].displayableName").doesNotExist(),
                jsonPath("$[2].lineUp").value(LineUp.MYTH_CLOTH.name()),
                jsonPath("$[2].series").value(Series.SAINT_SEIYA.name()),
                jsonPath("$[2].group").value(Group.GOLD.name()),
                jsonPath("$[2].metalBody").value(false),
                jsonPath("$[2].oce").value(false),
                jsonPath("$[2].revival").value(false),
                jsonPath("$[2].plainCloth").value(false),
                jsonPath("$[2].brokenCloth").value(false),
                jsonPath("$[2].bronzeToGold").value(false),
                jsonPath("$[2].gold").value(false),
                jsonPath("$[2].hongKongVersion").value(false),
                jsonPath("$[2].manga").value(false),
                jsonPath("$[2].surplice").value(false),
                jsonPath("$[2].set").value(false),
                jsonPath("$[2].anniversary").doesNotExist(),
                jsonPath("$[2].tags").doesNotExist(),
                jsonPath("$[2].images").doesNotExist(),
                jsonPath("$[2].issuanceJPY").exists(),
                jsonPath("$[2].issuanceJPY.basePrice").value("15000"),
                jsonPath("$[2].issuanceJPY.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[2].issuanceJPY.preorderDate").doesNotExist(),
                jsonPath("$[2].issuanceJPY.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[2].issuanceJPY.releaseDate").value("2024-04-04"),
                jsonPath("$[2].issuanceJPY.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[2].issuanceMXN.basePrice").doesNotExist(),
                jsonPath("$[2].issuanceMXN.firstAnnouncementDate").doesNotExist(),
                jsonPath("$[2].issuanceMXN.preorderDate").doesNotExist(),
                jsonPath("$[2].issuanceMXN.preorderConfirmationDay").doesNotExist(),
                jsonPath("$[2].issuanceMXN.releaseDate").doesNotExist(),
                jsonPath("$[2].issuanceMXN.releaseConfirmationDay").doesNotExist(),
                jsonPath("$[2].futureRelease").value(false),
                jsonPath("$[2].url").doesNotExist(),
                jsonPath("$[2].distribution").doesNotExist(),
                jsonPath("$[2].remarks").doesNotExist()
        );
        // @formatter:on
        return list.toArray(ResultMatcher[]::new);
    }

    @Test
    public void should_return_success_only_restocks_when_param_value_is_only() throws Exception {
        String id1 = UUID.randomUUID().toString();

        when(characterFigureService.retrieveAllCharacters(RestockType.ONLY)).thenReturn(
                List.of(createBasicMcCharacterFigure(id1, "Leo", new BigDecimal("10000"), LocalDate.of(2022, 1, 1))));

        // @formatter:off
        mockMvc.perform(get(BASE_URL)
                        .param("restocks", "ONLY"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].originalName").value("Leo"))
                .andExpect(jsonPath("$[0].baseName").value("Leo"))
                .andExpect(jsonPath("$[0].displayableName").doesNotExist())
                .andExpect(jsonPath("$[0].lineUp").value(LineUp.MYTH_CLOTH.name()))
                .andExpect(jsonPath("$[0].series").value(Series.SAINT_SEIYA.name()))
                .andExpect(jsonPath("$[0].group").value(Group.GOLD.name()))
                .andExpect(jsonPath("$[0].metalBody").value(false))
                .andExpect(jsonPath("$[0].oce").value(false))
                .andExpect(jsonPath("$[0].revival").value(false))
                .andExpect(jsonPath("$[0].plainCloth").value(false))
                .andExpect(jsonPath("$[0].brokenCloth").value(false))
                .andExpect(jsonPath("$[0].bronzeToGold").value(false))
                .andExpect(jsonPath("$[0].gold").value(false))
                .andExpect(jsonPath("$[0].hongKongVersion").value(false))
                .andExpect(jsonPath("$[0].manga").value(false))
                .andExpect(jsonPath("$[0].surplice").value(false))
                .andExpect(jsonPath("$[0].set").value(false))
                .andExpect(jsonPath("$[0].anniversary").doesNotExist())
                .andExpect(jsonPath("$[0].tags").doesNotExist())
                .andExpect(jsonPath("$[0].images").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY").exists())
                .andExpect(jsonPath("$[0].issuanceJPY.basePrice").value("10000"))
                .andExpect(jsonPath("$[0].issuanceJPY.firstAnnouncementDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.preorderDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.preorderConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.releaseDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].issuanceJPY.releaseConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.basePrice").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.firstAnnouncementDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.preorderDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.preorderConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.releaseDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.releaseConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].futureRelease").value(false))
                .andExpect(jsonPath("$[0].url").doesNotExist())
                .andExpect(jsonPath("$[0].distribution").doesNotExist())
                .andExpect(jsonPath("$[0].remarks").doesNotExist())
                ;
        // @formatter:on
    }

    @Test
    public void should_return_success_no_restocks_when_param_value_is_none() throws Exception {
        String id1 = UUID.randomUUID().toString();

        when(characterFigureService.retrieveAllCharacters(RestockType.NONE)).thenReturn(
                List.of(createBasicMcCharacterFigure(id1, "Virgo", new BigDecimal("11000"), LocalDate.of(2021, 3, 3))));

        // @formatter:off
        mockMvc.perform(get(BASE_URL)
                        .param("restocks", "NONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].originalName").value("Virgo"))
                .andExpect(jsonPath("$[0].baseName").value("Virgo"))
                .andExpect(jsonPath("$[0].displayableName").doesNotExist())
                .andExpect(jsonPath("$[0].lineUp").value(LineUp.MYTH_CLOTH.name()))
                .andExpect(jsonPath("$[0].series").value(Series.SAINT_SEIYA.name()))
                .andExpect(jsonPath("$[0].group").value(Group.GOLD.name()))
                .andExpect(jsonPath("$[0].metalBody").value(false))
                .andExpect(jsonPath("$[0].oce").value(false))
                .andExpect(jsonPath("$[0].revival").value(false))
                .andExpect(jsonPath("$[0].plainCloth").value(false))
                .andExpect(jsonPath("$[0].brokenCloth").value(false))
                .andExpect(jsonPath("$[0].bronzeToGold").value(false))
                .andExpect(jsonPath("$[0].gold").value(false))
                .andExpect(jsonPath("$[0].hongKongVersion").value(false))
                .andExpect(jsonPath("$[0].manga").value(false))
                .andExpect(jsonPath("$[0].surplice").value(false))
                .andExpect(jsonPath("$[0].set").value(false))
                .andExpect(jsonPath("$[0].anniversary").doesNotExist())
                .andExpect(jsonPath("$[0].tags").doesNotExist())
                .andExpect(jsonPath("$[0].images").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY").exists())
                .andExpect(jsonPath("$[0].issuanceJPY.basePrice").value("11000"))
                .andExpect(jsonPath("$[0].issuanceJPY.firstAnnouncementDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.preorderDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.preorderConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceJPY.releaseDate").value("2021-03-03"))
                .andExpect(jsonPath("$[0].issuanceJPY.releaseConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.basePrice").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.firstAnnouncementDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.preorderDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.preorderConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.releaseDate").doesNotExist())
                .andExpect(jsonPath("$[0].issuanceMXN.releaseConfirmationDay").doesNotExist())
                .andExpect(jsonPath("$[0].futureRelease").value(false))
                .andExpect(jsonPath("$[0].url").doesNotExist())
                .andExpect(jsonPath("$[0].distribution").doesNotExist())
                .andExpect(jsonPath("$[0].remarks").doesNotExist())
        ;
        // @formatter:on
    }

}
