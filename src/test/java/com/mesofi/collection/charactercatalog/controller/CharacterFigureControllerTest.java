package com.mesofi.collection.charactercatalog.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;

@ActiveProfiles("test")
@WebMvcTest(CharacterFigureController.class)
public class CharacterFigureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharacterFigureService characterFigureService;

    final String BASE_URL = "/characters";

    @Test
    public void should_ReturnStatusesOK_WhenDataFound() throws Exception {

        List<CharacterFigure> list = new ArrayList<>();
        list.add(createBasicEXCharacterFigure("62f3209c7b1da7352ea751b7", "Siren Sorrento", new BigDecimal("9500")));
        list.add(createBasicEXCharacterFigure("62dc96905e86015074a010eb", "Aries Mu", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62dc99715e86015074a010ef", "Sagittarius", new BigDecimal("6500")));
        list.add(createBasicEXCharacterFigure("62f43d557b1da7352ea751ef", "Dragon Shiryu", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62dc96f15e86015074a010ec", "Scorpio Milo", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62f438127b1da7352ea751e7", "Virgo Shaka", new BigDecimal("6500")));

        when(characterFigureService.retrieveAllCharacters(null)).thenReturn(list);

        mockMvc.perform(get(BASE_URL)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(6))).andExpect(jsonPath("$[0].id").value("62f3209c7b1da7352ea751b7"))
                .andExpect(jsonPath("$[0].name").value("Siren Sorrento"))
                .andExpect(jsonPath("$[0].basePrice").value("9500"))
                .andExpect(jsonPath("$[1].id").value("62dc96905e86015074a010eb"))
                .andExpect(jsonPath("$[1].name").value("Aries Mu")).andExpect(jsonPath("$[1].basePrice").value("6000"))
                .andExpect(jsonPath("$[2].id").value("62dc99715e86015074a010ef"))
                .andExpect(jsonPath("$[2].name").value("Sagittarius"))
                .andExpect(jsonPath("$[2].basePrice").value("6500"))
                .andExpect(jsonPath("$[3].id").value("62f43d557b1da7352ea751ef"))
                .andExpect(jsonPath("$[3].name").value("Dragon Shiryu"))
                .andExpect(jsonPath("$[3].basePrice").value("6000"))
                .andExpect(jsonPath("$[4].id").value("62dc96f15e86015074a010ec"))
                .andExpect(jsonPath("$[4].name").value("Scorpio Milo"))
                .andExpect(jsonPath("$[4].basePrice").value("6000"))
                .andExpect(jsonPath("$[5].id").value("62f438127b1da7352ea751e7"))
                .andExpect(jsonPath("$[5].name").value("Virgo Shaka"))
                .andExpect(jsonPath("$[5].basePrice").value("6500"));
    }

    private CharacterFigure createBasicEXCharacterFigure(String id, String name, BigDecimal basePrice) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 9, 27, 11, 11, 11);

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setId(id);
        characterFigure.setName(name);
        characterFigure.setReleaseDate(calendar.getTime());
        characterFigure.setBasePrice(basePrice);
        characterFigure.setPrice(new BigDecimal("99.99"));
        characterFigure.setTax(new BigDecimal(".10"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setDistribution(Distribution.GENERAL);

        return characterFigure;
    }
}
