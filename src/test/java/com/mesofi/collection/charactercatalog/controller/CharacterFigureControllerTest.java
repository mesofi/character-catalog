package com.mesofi.collection.charactercatalog.controller;

import static com.mesofi.collection.charactercatalog.MockData.fromObjectToJson;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mesofi.collection.charactercatalog.exceptions.NoSuchCharacterFoundException;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Restock;
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
    public void should_ReturnBadRequest_WhenBodyIsMissing() throws Exception {
        // @formatter:off
        mockMvc.perform(post(BASE_URL)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Required request body is missing")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty());
        // @formatter:on
    }

    @Test
    public void should_ReturnUnsupportedMediaType_WhenBodyIsEmpty() throws Exception {
        // @formatter:off
        mockMvc.perform(post(BASE_URL).content("{}")).andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name()))
                .andExpect(jsonPath("$.message").value(containsString("'application/octet-stream' not supported")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty());
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenBodyIsEmpty() throws Exception {
        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{}")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(6)))
                .andExpect(jsonPath("$.errors", hasItem("name: is required with at least 2 characters")))
                .andExpect(jsonPath("$.errors", hasItem("releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or greater than 6 months from now")))
                .andExpect(jsonPath("$.errors", hasItem("basePrice: is required and must have a positive value")))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenNameIsOutOfBounds() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("A"); // name is too short
        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(6)))
                .andExpect(jsonPath("$.errors", hasItem("name: must be between 2 and 200")))
                .andExpect(jsonPath("$.errors", hasItem("releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or greater than 6 months from now")))
                .andExpect(jsonPath("$.errors", hasItem("basePrice: is required and must have a positive value")))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenReleaseDateIsTooOld() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        characterFigure.setReleaseDate(calendar.getTime());

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors", hasItem("releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or greater than 6 months from now")))
                .andExpect(jsonPath("$.errors", hasItem("basePrice: is required and must have a positive value")))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenReleaseDateIsInTheFuture() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 7);
        characterFigure.setReleaseDate(calendar.getTime());

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors", hasItem("releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or greater than 6 months from now")))
                .andExpect(jsonPath("$.errors", hasItem("basePrice: is required and must have a positive value")))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenBasePriceIsNegative() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        characterFigure.setReleaseDate(new Date());
        characterFigure.setBasePrice(new BigDecimal(-1));

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("basePrice: is required and must have a positive value")))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenTaxIsNegative() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        characterFigure.setReleaseDate(new Date());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal(-1));

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("tax: is required and must have a decimal value")))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenLineUpIsMissing() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        characterFigure.setReleaseDate(new Date());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal("0.10"));

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem("lineUp: is required")))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenDistributionIsMissing() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        characterFigure.setReleaseDate(new Date());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal("0.10"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("distribution: is required")));
        // @formatter:on
    }

    @Test
    public void should_CreateNewCharacter_WhenDataIsProvided() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 3);
        characterFigure.setReleaseDate(calendar.getTime());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal("0.10"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setDistribution(Distribution.GENERAL);
        characterFigure.setUrl("https://tamashii.jp/item/13721/");

        String requestJson = fromObjectToJson(characterFigure);

        CharacterFigure characterFigureCreated = new CharacterFigure();
        characterFigureCreated.setId("62d4658bff17ae100e217e50");
        characterFigureCreated.setName(characterFigure.getName());
        characterFigureCreated.setReleaseDate(characterFigure.getReleaseDate());
        characterFigureCreated.setBasePrice(characterFigure.getBasePrice());
        characterFigureCreated.setTax(characterFigure.getTax());
        characterFigureCreated.setPrice(new BigDecimal("7700"));
        characterFigureCreated.setLineUp(characterFigure.getLineUp());
        characterFigureCreated.setDistribution(characterFigure.getDistribution());
        characterFigureCreated.setUrl(characterFigure.getUrl());

        when(characterFigureService.createNewCharacter(characterFigure)).thenReturn(characterFigureCreated);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("62d4658bff17ae100e217e50"))
                .andExpect(jsonPath("$.name").value(characterFigureCreated.getName()))
                .andExpect(jsonPath("$.releaseDate").value(containsString("2022-04-03")))
                .andExpect(jsonPath("$.basePrice").value("7000"))
                .andExpect(jsonPath("$.tax").value("0.1"))
                .andExpect(jsonPath("$.price").value("7700"))
                .andExpect(jsonPath("$.lineUp").value("MYTH_CLOTH_EX"))
                .andExpect(jsonPath("$.distribution").value("GENERAL"))
                .andExpect(jsonPath("$.url").value("https://tamashii.jp/item/13721/"));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenRestockReleaseDateIsMissing() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 3);
        characterFigure.setReleaseDate(calendar.getTime());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal("0.10"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setDistribution(Distribution.GENERAL);
        characterFigure.setUrl("https://tamashii.jp/item/13721/");

        List<Restock> restocks = new ArrayList<>();
        restocks.add(new Restock()); // it's missing

        characterFigure.setRestocks(restocks);

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed for argument")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("restocks[0].releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or a future date"));
        // @formatter:on
    }

    @Test
    public void should_ReturnBadRequest_WhenRestockReleaseDateIsFutureDay() throws Exception {
        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 3);
        characterFigure.setReleaseDate(calendar.getTime());
        characterFigure.setBasePrice(new BigDecimal(7000));
        characterFigure.setTax(new BigDecimal("0.10"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigure.setDistribution(Distribution.GENERAL);
        characterFigure.setUrl("https://tamashii.jp/item/13721/");

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 1); // 1 month is added

        Restock restock = new Restock();
        restock.setReleaseDate(calendar2.getTime());// future day

        List<Restock> restocks = new ArrayList<>();
        restocks.add(restock); // future date

        characterFigure.setRestocks(restocks);

        String requestJson = fromObjectToJson(characterFigure);

        // @formatter:off
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(containsString("Validation failed for argument")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("restocks[0].releaseDate: is required yyyy-MM-dd and should not be less than 2003-11-01 or a future date"));
        // @formatter:on
    }

    @Test
    public void should_ReturnOK_WhenDataFound() throws Exception {

        List<CharacterFigure> list = new ArrayList<>();
        list.add(createBasicEXCharacterFigure("62f3209c7b1da7352ea751b7", "Siren Sorrento", new BigDecimal("9500")));
        list.add(createBasicEXCharacterFigure("62dc96905e86015074a010eb", "Aries Mu", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62dc99715e86015074a010ef", "Sagittarius", new BigDecimal("6500")));
        list.add(createBasicEXCharacterFigure("62f43d557b1da7352ea751ef", "Dragon Shiryu", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62dc96f15e86015074a010ec", "Scorpio Milo", new BigDecimal("6000")));
        list.add(createBasicEXCharacterFigure("62f438127b1da7352ea751e7", "Virgo Shaka", new BigDecimal("6500")));

        when(characterFigureService.retrieveAllCharacters(null)).thenReturn(list);

        // @formatter:off
        mockMvc.perform(get(BASE_URL)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id").value("62f3209c7b1da7352ea751b7"))
                .andExpect(jsonPath("$[0].name").value("Siren Sorrento"))
                .andExpect(jsonPath("$[0].basePrice").value("9500"))
                .andExpect(jsonPath("$[1].id").value("62dc96905e86015074a010eb"))
                .andExpect(jsonPath("$[1].name").value("Aries Mu"))
                .andExpect(jsonPath("$[1].basePrice").value("6000"))
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
        // @formatter:on
    }

    @Test
    public void should_ReturnOK_WhenDataFoundByName() throws Exception {
        String theName = "Aries Mu";
        List<CharacterFigure> list = new ArrayList<>();
        list.add(createBasicEXCharacterFigure("62dc96905e86015074a010eb", theName, new BigDecimal("6000")));

        when(characterFigureService.retrieveAllCharacters(theName)).thenReturn(list);

        // @formatter:off
        mockMvc.perform(get(BASE_URL).param("name", theName)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("62dc96905e86015074a010eb"))
                .andExpect(jsonPath("$[0].name").value(theName))
                .andExpect(jsonPath("$[0].basePrice").value("6000"));
        // @formatter:on
    }

    @Test
    public void should_ReturnNotFound_WhenIdDoesNotExist() throws Exception {
        String id = "incorrectId";

        String msg = "Character not found: " + id;
        when(characterFigureService.retrieveCharacterById(id)).thenThrow(new NoSuchCharacterFoundException(msg));

        // @formatter:off
        mockMvc.perform(get(BASE_URL + "/" + id)).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(containsString(msg)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty());
        ;
        // @formatter:on
    }

    @Test
    public void should_ReturnOK_WhenCharacterExists() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.MAY, 1);

        String id = "62d4658bff17ae100e217e50";

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setId(id);
        characterFigure.setName("Hypnos");
        characterFigure.setReleaseDate(calendar.getTime());
        characterFigure.setBasePrice(new BigDecimal("7800"));
        characterFigure.setTax(new BigDecimal("0.05"));
        characterFigure.setLineUp(LineUp.MYTH_CLOTH);
        characterFigure.setDistribution(Distribution.GENERAL);
        characterFigure.setUrl("https://tamashii.jp/item/982/");
        List<Restock> restocks = new ArrayList<>();
        Restock restock = new Restock();
        restock.setReleaseDate(calendar.getTime());
        restocks.add(restock);
        characterFigure.setRestocks(restocks);

        when(characterFigureService.retrieveCharacterById(id)).thenReturn(characterFigure);

        // @formatter:off
        mockMvc.perform(get(BASE_URL + "/" + id)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hypnos"))
                .andExpect(jsonPath("$.releaseDate").value(containsString("2022-05-01")))
                .andExpect(jsonPath("$.basePrice").value("7800"))
                .andExpect(jsonPath("$.tax").value("0.05"))
                .andExpect(jsonPath("$.lineUp").value("MYTH_CLOTH"))
                .andExpect(jsonPath("$.distribution").value("GENERAL"))
                .andExpect(jsonPath("$.url").value("https://tamashii.jp/item/982/"))
                .andExpect(jsonPath("$.restocks").isArray())
                .andExpect(jsonPath("$.restocks", hasSize(1)))
                .andExpect(jsonPath("$.restocks[0].releaseDate").value(containsString("2022-05-01")));
        // @formatter:on
    }

    @Test
    public void should_UpdateAndReturnOK_WhenCharacterExists() throws Exception {
        List<Restock> restocks = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, Calendar.JANUARY, 7);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2022, Calendar.JULY, 8);

        restocks.add(createBasicRestock(calendar1.getTime(), "https://tamashii.jp/item/13721/", "Some comment"));
        restocks.add(createBasicRestock(calendar2.getTime(), "https://tamashii.jp/item/13333/", "Another comment"));

        String requestJson = fromObjectToJson(restocks);
        String id = "62d4658bff17ae100e217e50";

        CharacterFigure characterFigureUpdated = new CharacterFigure();
        characterFigureUpdated.setId(id);
        characterFigureUpdated.setName("Seiya");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 3);
        characterFigureUpdated.setReleaseDate(calendar.getTime());
        characterFigureUpdated.setBasePrice(new BigDecimal(7000));
        characterFigureUpdated.setTax(new BigDecimal("0.10"));
        characterFigureUpdated.setLineUp(LineUp.MYTH_CLOTH_EX);
        characterFigureUpdated.setDistribution(Distribution.GENERAL);
        characterFigureUpdated.setUrl("https://tamashii.jp/item/13721/");
        characterFigureUpdated.setRestocks(restocks);

        when(characterFigureService.updateCharacterRestock(id, restocks)).thenReturn(characterFigureUpdated);

        // @formatter:off
        mockMvc.perform(patch(BASE_URL + "/" + id + "/restocks").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restocks").isArray())
                .andExpect(jsonPath("$.restocks", hasSize(2)))
                .andExpect(jsonPath("$.restocks[0].releaseDate").value(containsString("2022-01-08")))
                .andExpect(jsonPath("$.restocks[0].basePrice").doesNotExist())
                .andExpect(jsonPath("$.restocks[0].distribution").value("GENERAL"))
                .andExpect(jsonPath("$.restocks[0].url").value("https://tamashii.jp/item/13721/"))
                .andExpect(jsonPath("$.restocks[0].remarks").value("Some comment"))
                .andExpect(jsonPath("$.restocks[1].releaseDate").value(containsString("2022-07-08")))
                .andExpect(jsonPath("$.restocks[1].basePrice").doesNotExist())
                .andExpect(jsonPath("$.restocks[1].distribution").value("GENERAL"))
                .andExpect(jsonPath("$.restocks[1].url").value("https://tamashii.jp/item/13333/"))
                .andExpect(jsonPath("$.restocks[1].remarks").value("Another comment"))
        ;
        // @formatter:on
    }

    private Restock createBasicRestock(Date releaseDate, String url, String remarks) {
        Restock restock = new Restock();
        restock.setReleaseDate(releaseDate);
        restock.setRemarks(remarks);
        restock.setUrl(url);
        restock.setDistribution(Distribution.GENERAL);
        return restock;
    }

    private CharacterFigure createBasicEXCharacterFigure(String id, String name, BigDecimal basePrice) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.OCTOBER, 27, 11, 11, 11);

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
