/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Feb 5, 2024.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.utils.CommonUtils.isDayMonthYear;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toDate;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toInteger;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toPrice;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toStringValue;
import static com.mesofi.collection.charactercatalog.utils.FileUtils.getPathFromClassPath;
import static com.mesofi.collection.charactercatalog.utils.MockUtils.getNamingExclusions;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.mesofi.collection.charactercatalog.CharacterCatalogConfig;
import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockType;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test {@link CharacterFigureService}
 *
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class CharacterFigureServiceTest {

  @InjectMocks private CharacterFigureService service;

  @Mock private MultipartFile multipartFile;

  @Mock private CharacterFigureRepository repo;
  @Mock private CharacterFigureModelMapper modelMapper;
  @Mock private CharacterFigureFileMapper fileMapper;
  @Mock private CharacterCatalogConfig.Props props;

  /** Test for {@link CharacterFigureService#loadAllCharacters(MultipartFile)} */
  @Test
  public void loadAllCharacters_whenFileIsMissing_thenThrowIllegalArgumentException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> service.loadAllCharacters(null))
        .withMessage("The uploaded file is missing...");
  }

  /**
   * Test for {@link CharacterFigureService#loadAllCharacters(MultipartFile)}
   *
   * @throws IOException Thrown when there was an error during the load.
   */
  @Test
  public void loadAllCharacters_whenUnableToOpenFile_thenThrowCharacterFigureException()
      throws IOException {
    when(multipartFile.getInputStream()).thenThrow(new IOException("Error opening the file"));

    assertThatExceptionOfType(CharacterFigureException.class)
        .isThrownBy(() -> service.loadAllCharacters(multipartFile))
        .withMessage("Unable to read characters from initial input file");
  }

  /**
   * Test for {@link CharacterFigureService#loadAllCharacters(MultipartFile)}
   *
   * @throws IOException Thrown when there was an error during the load.
   */
  @Test
  public void loadAllCharacters_whenTinyFileIsPresent_thenSuccess() throws IOException {
    final String folder = "characters/";
    final String name = "MythCloth Catalog - CatalogMyth-tiny.tsv";
    final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));

    InputStream inputStream = new ByteArrayInputStream(bytes);
    when(multipartFile.getInputStream()).thenReturn(inputStream);

    CharacterFigure cf1 = new CharacterFigure();
    cf1.setOriginalName("Eta Benetnasch Mime EX");
    cf1.setBaseName("Eta Benetnasch Mime");
    cf1.setIssuanceJPY(createIssuance("¥0", "", "", ""));
    cf1.setIssuanceMXN(createIssuance("", null, "", ""));
    cf1.setFutureRelease(!StringUtils.hasText(""));
    cf1.setUrl(toStringValue(null));
    cf1.setDistribution(null);
    cf1.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf1.setSeries(Series.SAINT_SEIYA);
    cf1.setGroup(Group.ROBE);
    cf1.setMetalBody(false);
    cf1.setOce(false);
    cf1.setRevival(false);
    cf1.setPlainCloth(false);
    cf1.setBrokenCloth(false);
    cf1.setBronzeToGold(false);
    cf1.setGold(false);
    cf1.setHongKongVersion(false);
    cf1.setManga(false);
    cf1.setSurplice(false);
    cf1.setSet(false);
    cf1.setAnniversary(null);
    cf1.setRemarks(null);
    cf1.setTags(null);
    cf1.setImages(null);
    when(fileMapper.fromLineToCharacterFigure(
            "Eta Benetnasch Mime EX\tEta Benetnasch Mime\t¥0\t¥0\t\t\t\t\t\t\t\t\tMyth Cloth EX\tSaint Seiya\tGod Robe\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE"))
        .thenReturn(cf1);

    CharacterFigure cf2 = createCharacterFigureWithReleaseDate("7/2024");
    when(fileMapper.fromLineToCharacterFigure(
            "Sagittarius Seiya ~Inheritor of the Gold Cloth~ EX\tSagittarius Seiya ~Inheritor of the Gold Cloth~\t¥20,000\t¥22,000\t12/28/2023\t1/10/2024\t7/2024\t\t\t\thttps://tamashiiweb.com/item/14738\tStores\tMyth Cloth EX\tSaint Seiya\tGold Saint\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t924/b2TERq"))
        .thenReturn(cf2);

    CharacterFigure cf3 = new CharacterFigure();
    cf3.setOriginalName("Pegasus Seiya ~Knights of the Zodiac~ EX");
    cf3.setBaseName("Pegasus Seiya ~Knights of the Zodiac~");
    cf3.setIssuanceJPY(createIssuance("¥18,000", "11/7/2023", "11/21/2023", "5/2024"));
    cf3.setIssuanceMXN(createIssuance("", null, "", ""));
    cf3.setFutureRelease(!StringUtils.hasText("5/2024"));
    cf3.setUrl(toStringValue("https://tamashiiweb.com/item/14676"));
    cf3.setDistribution(Distribution.STORES);
    cf3.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf3.setSeries(Series.THE_BEGINNING);
    cf3.setGroup(Group.OTHER);
    cf3.setMetalBody(false);
    cf3.setOce(false);
    cf3.setRevival(false);
    cf3.setPlainCloth(false);
    cf3.setBrokenCloth(false);
    cf3.setBronzeToGold(false);
    cf3.setGold(false);
    cf3.setHongKongVersion(false);
    cf3.setManga(false);
    cf3.setSurplice(false);
    cf3.setSet(false);
    cf3.setAnniversary(null);
    cf3.setRemarks(null);
    cf3.setTags(null);
    cf3.setImages(List.of(new GalleryImage(null, "923/gqkNEn", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Pegasus Seiya ~Knights of the Zodiac~ EX\tPegasus Seiya ~Knights of the Zodiac~\t¥18,000\t¥19,800\t11/7/2023\t11/21/2023\t5/2024\t\t\t\thttps://tamashiiweb.com/item/14676\tStores\tMyth Cloth EX\tSaint Seiya The Beginning\t-\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t923/gqkNEn"))
        .thenReturn(cf3);

    CharacterFigureEntity cfe1 = new CharacterFigureEntity();
    cfe1.setId("1000000001");
    cfe1.setOriginalName("Eta Benetnasch Mime EX");
    cfe1.setBaseName("Eta Benetnasch Mime");
    cfe1.setIssuanceJPY(createIssuance("¥0", "", "", ""));
    cfe1.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe1.setFutureRelease(!StringUtils.hasText(""));
    cfe1.setUrl(toStringValue(null));
    cfe1.setDistribution(null);
    cfe1.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe1.setSeries(Series.SAINT_SEIYA);
    cfe1.setGroup(Group.ROBE);
    cfe1.setMetal(false);
    cfe1.setOce(false);
    cfe1.setRevival(false);
    cfe1.setPlainCloth(false);
    cfe1.setBrokenCloth(false);
    cfe1.setGolden(false);
    cfe1.setGold(false);
    cfe1.setHk(false);
    cfe1.setManga(false);
    cfe1.setSurplice(false);
    cfe1.setSet(false);
    cfe1.setAnniversary(null);
    cfe1.setRemarks(null);
    cfe1.setTags(null);
    cfe1.setImages(null);
    when(modelMapper.toEntity(cf1)).thenReturn(cfe1);

    CharacterFigureEntity cfe2 = createCharacterFigureEntityWithReleaseDate("1000000002", "7/2024");
    when(modelMapper.toEntity(cf2)).thenReturn(cfe2);

    CharacterFigureEntity cfe3 = new CharacterFigureEntity();
    cfe3.setId("1000000003");
    cfe3.setOriginalName("Pegasus Seiya ~Knights of the Zodiac~ EX");
    cfe3.setBaseName("Pegasus Seiya ~Knights of the Zodiac~");
    cfe3.setIssuanceJPY(createIssuance("¥18,000", "11/7/2023", "11/21/2023", "5/2024"));
    cfe3.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe3.setFutureRelease(!StringUtils.hasText("5/2024"));
    cfe3.setUrl(toStringValue("https://tamashiiweb.com/item/14676"));
    cfe3.setDistribution(Distribution.STORES);
    cfe3.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe3.setSeries(Series.THE_BEGINNING);
    cfe3.setGroup(Group.OTHER);
    cfe3.setMetal(false);
    cfe3.setOce(false);
    cfe3.setRevival(false);
    cfe3.setPlainCloth(false);
    cfe3.setBrokenCloth(false);
    cfe3.setGolden(false);
    cfe3.setGold(false);
    cfe3.setHk(false);
    cfe3.setManga(false);
    cfe3.setSurplice(false);
    cfe3.setSet(false);
    cfe3.setAnniversary(null);
    cfe3.setRemarks(null);
    cfe3.setTags(null);
    cfe3.setImages(List.of(new GalleryImage(null, "923/gqkNEn", true, false, 0)));
    when(modelMapper.toEntity(cf3)).thenReturn(cfe3);

    doNothing().when(repo).deleteAll();
    when(repo.saveAll(any())).thenReturn(List.of(cfe1, cfe2, cfe3));

    // The service is tested here ...
    assertEquals(3, service.loadAllCharacters(multipartFile));
  }

  /**
   * Test for {@link CharacterFigureService#convertStreamToEntityList(java.io.InputStream)}
   *
   * @throws IOException Thrown when there was an error during the load.
   */
  @ParameterizedTest
  @ValueSource(
      strings = {
        "MythCloth Catalog - CatalogMyth-min-empty-no-headers.tsv",
        "MythCloth Catalog - CatalogMyth-min-empty-with-headers.tsv"
      })
  public void convertStreamToEntityList_whenEmptyFile_thenEntityEmpty(String name)
      throws IOException {
    final String folder = "characters/";
    final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));

    // The service is tested here ...
    List<CharacterFigureEntity> entity =
        service.convertStreamToEntityList(new ByteArrayInputStream(bytes));
    assertNotNull(entity);
    assertEquals(0, entity.size());
  }

  /**
   * Test for {@link CharacterFigureService#convertStreamToEntityList(java.io.InputStream)}
   *
   * @throws IOException Thrown when there was an error during the load.
   */
  @Test
  public void convertStreamToEntityList_whenMinFile_thenSuccess() throws IOException {
    final String folder = "characters/";
    final String name = "MythCloth Catalog - CatalogMyth-min.tsv";
    final byte[] bytes = Files.readAllBytes(getPathFromClassPath(folder + name));

    CharacterFigure cf1 = new CharacterFigure();
    cf1.setOriginalName("Libra Dohko EX <Revival>");
    cf1.setBaseName("Libra Dohko");
    cf1.setIssuanceJPY(createIssuance("¥22,000", "8/30/2023", "9/1/2023", "2/2024"));
    cf1.setIssuanceMXN(createIssuance("", null, "", ""));
    cf1.setFutureRelease(!StringUtils.hasText("2/2024"));
    cf1.setUrl(toStringValue("https://tamashiiweb.com/item/14590"));
    cf1.setDistribution(Distribution.STORES);
    cf1.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf1.setSeries(Series.SAINT_SEIYA);
    cf1.setGroup(Group.GOLD);
    cf1.setMetalBody(false);
    cf1.setOce(false);
    cf1.setRevival(true);
    cf1.setPlainCloth(false);
    cf1.setBrokenCloth(false);
    cf1.setBronzeToGold(false);
    cf1.setGold(false);
    cf1.setHongKongVersion(false);
    cf1.setManga(false);
    cf1.setSurplice(false);
    cf1.setSet(false);
    cf1.setAnniversary(null);
    cf1.setRemarks(null);
    cf1.setTags(null);
    cf1.setImages(List.of(new GalleryImage(null, "923/TcGPcH", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Libra Dohko EX <Revival>\tLibra Dohko\t¥22,000\t¥24,200\t8/30/2023\t9/1/2023\t2/2024\t\t\t\thttps://tamashiiweb.com/item/14590\tStores\tMyth Cloth EX\tSaint Seiya\tGold Saint\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t923/TcGPcH"))
        .thenReturn(cf1);

    CharacterFigure cf2 = new CharacterFigure();
    cf2.setOriginalName("Pegasus Seiya ~Initial Bronze Cloth 20th Anniversary Ver.~");
    cf2.setBaseName("Pegasus Seiya");
    cf2.setIssuanceJPY(createIssuance("¥12,500", "9/7/2023", "9/8/2023", "1/2024"));
    cf2.setIssuanceMXN(createIssuance("", null, "", ""));
    cf2.setFutureRelease(!StringUtils.hasText("1/2024"));
    cf2.setUrl(toStringValue("https://tamashiiweb.com/item/14566"));
    cf2.setDistribution(Distribution.TAMASHII_WEB_SHOP);
    cf2.setLineUp(LineUp.MYTH_CLOTH);
    cf2.setSeries(Series.SAINT_SEIYA);
    cf2.setGroup(Group.V1);
    cf2.setMetalBody(false);
    cf2.setOce(false);
    cf2.setRevival(false);
    cf2.setPlainCloth(false);
    cf2.setBrokenCloth(false);
    cf2.setBronzeToGold(false);
    cf2.setGold(false);
    cf2.setHongKongVersion(false);
    cf2.setManga(false);
    cf2.setSurplice(false);
    cf2.setSet(false);
    cf2.setAnniversary(toInteger("20"));
    cf2.setRemarks("20th anniversary");
    cf2.setTags(null);
    cf2.setImages(List.of(new GalleryImage(null, "924/n9EbWm", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Pegasus Seiya ~Initial Bronze Cloth 20th Anniversary Ver.~\tPegasus Seiya\t¥12,500\t¥13,750\t9/7/2023\t9/8/2023\t1/2024\t\t\t\thttps://tamashiiweb.com/item/14566\tTamashii Web Shop\tMyth Cloth\tSaint Seiya\tBronze Saint V1\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t20\t20th anniversary\t\t924/n9EbWm"))
        .thenReturn(cf2);

    CharacterFigure cf3 = new CharacterFigure();
    cf3.setOriginalName("Goddess Athena & Saori Kido -Divine Saga Premium Set- EX");
    cf3.setBaseName("Goddess Athena & Saori Kido ~Divine Saga Premium Set~");
    cf3.setIssuanceJPY(createIssuance("¥32,000", "", "7/3/2023", "12/23/2024"));
    cf3.setIssuanceMXN(createIssuance("", null, "", ""));
    cf3.setFutureRelease(!StringUtils.hasText("12/23/2023"));
    cf3.setUrl(toStringValue("https://tamashiiweb.com/item/14469"));
    cf3.setDistribution(Distribution.STORES);
    cf3.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf3.setSeries(Series.SAINT_SEIYA);
    cf3.setGroup(Group.GOD);
    cf3.setMetalBody(true);
    cf3.setOce(false);
    cf3.setRevival(false);
    cf3.setPlainCloth(false);
    cf3.setBrokenCloth(false);
    cf3.setBronzeToGold(false);
    cf3.setGold(false);
    cf3.setHongKongVersion(false);
    cf3.setManga(false);
    cf3.setSurplice(false);
    cf3.setSet(true);
    cf3.setAnniversary(null);
    cf3.setRemarks("20th anniversary");
    cf3.setTags(null);
    cf3.setImages(List.of(new GalleryImage(null, "923/Mk6Y9K", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Goddess Athena & Saori Kido -Divine Saga Premium Set- EX\tGoddess Athena & Saori Kido ~Divine Saga Premium Set~\t¥32,000\t¥35,200\t\t7/3/2023\t12/23/2023\t\t\t\thttps://tamashiiweb.com/item/14469\tStores\tMyth Cloth EX\tSaint Seiya\tGod\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\t\t20th anniversary\t\t923/Mk6Y9K"))
        .thenReturn(cf3);

    CharacterFigure cf4 = new CharacterFigure();
    cf4.setOriginalName("Taurus Aldebaran (God Cloth) EX");
    cf4.setBaseName("Taurus Aldebaran");
    cf4.setIssuanceJPY(createIssuance("¥16,000", "", "5/7/2016", "11/26/2016"));
    cf4.setIssuanceMXN(createIssuance("", null, "", ""));
    cf4.setFutureRelease(!StringUtils.hasText("11/26/2016"));
    cf4.setUrl(toStringValue("https://tamashiiweb.com/item/11447"));
    cf4.setDistribution(Distribution.STORES);
    cf4.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf4.setSeries(Series.SOG);
    cf4.setGroup(Group.GOLD);
    cf4.setMetalBody(false);
    cf4.setOce(false);
    cf4.setRevival(false);
    cf4.setPlainCloth(false);
    cf4.setBrokenCloth(false);
    cf4.setBronzeToGold(false);
    cf4.setGold(false);
    cf4.setHongKongVersion(false);
    cf4.setManga(false);
    cf4.setSurplice(false);
    cf4.setSet(false);
    cf4.setAnniversary(null);
    cf4.setRemarks(null);
    cf4.setTags(null);
    cf4.setImages(List.of(new GalleryImage(null, "924/vqMu0V", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Taurus Aldebaran (God Cloth) EX\tTaurus Aldebaran\t¥16,000\t¥17,600\t\t5/7/2016\t11/26/2016\t\t\t\thttps://tamashiiweb.com/item/11447\tStores\tMyth Cloth EX\tSoul of Gold\tGold Saint\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\t\t\t924/vqMu0V"))
        .thenReturn(cf4);

    CharacterFigure cf5 = new CharacterFigure();
    cf5.setOriginalName("Pegasus Seiya & Goddess Athena ~Broken~ OCE");
    cf5.setBaseName("Pegasus Seiya & Goddess Athena ~Broken~");
    cf5.setIssuanceJPY(createIssuance("¥7,500", "", "", "2/11/2011"));
    cf5.setIssuanceMXN(createIssuance("", null, "", ""));
    cf5.setFutureRelease(!StringUtils.hasText("2/11/2011"));
    cf5.setUrl(toStringValue(null));
    cf5.setDistribution(Distribution.TAMASHII_NATIONS);
    cf5.setLineUp(LineUp.MYTH_CLOTH);
    cf5.setSeries(Series.SAINT_SEIYA);
    cf5.setGroup(Group.OTHER);
    cf5.setMetalBody(false);
    cf5.setOce(true);
    cf5.setRevival(false);
    cf5.setPlainCloth(false);
    cf5.setBrokenCloth(true);
    cf5.setBronzeToGold(false);
    cf5.setGold(false);
    cf5.setHongKongVersion(false);
    cf5.setManga(false);
    cf5.setSurplice(false);
    cf5.setSet(false);
    cf5.setAnniversary(null);
    cf5.setRemarks("Tamashii Features 2011");
    cf5.setTags(null);
    cf5.setImages(List.of(new GalleryImage(null, "924/rgPypv", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Pegasus Seiya & Goddess Athena ~Broken~ OCE\tPegasus Seiya & Goddess Athena ~Broken~\t¥7,500\t¥8,250\t\t\t2/11/2011\t\t\t\t\tTamashii Nations\tMyth Cloth\tSaint Seiya\t-\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\t\tTamashii Features 2011\t\t924/rgPypv"))
        .thenReturn(cf5);

    CharacterFigure cf6 = new CharacterFigure();
    cf6.setOriginalName("Pegasus Seiya ~New Bronze Cloth~ Golden Limited Edition ~HK Version~ EX");
    cf6.setBaseName("Pegasus Seiya");
    cf6.setIssuanceJPY(createIssuance("¥600", "", "", "8/8/2016"));
    cf6.setIssuanceMXN(createIssuance("", null, "", ""));
    cf6.setFutureRelease(!StringUtils.hasText("8/8/2016"));
    cf6.setUrl(toStringValue(null));
    cf6.setDistribution(Distribution.OTHER);
    cf6.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf6.setSeries(Series.SAINT_SEIYA);
    cf6.setGroup(Group.V2);
    cf6.setMetalBody(false);
    cf6.setOce(false);
    cf6.setRevival(false);
    cf6.setPlainCloth(false);
    cf6.setBrokenCloth(false);
    cf6.setBronzeToGold(true);
    cf6.setGold(false);
    cf6.setHongKongVersion(true);
    cf6.setManga(false);
    cf6.setSurplice(false);
    cf6.setSet(false);
    cf6.setAnniversary(null);
    cf6.setRemarks("Only in China: https://tamashiiweb.com/event/182");
    cf6.setTags(null);
    cf6.setImages(List.of(new GalleryImage(null, "922/EWuoQm", true, false, 0)));
    when(fileMapper.fromLineToCharacterFigure(
            "Pegasus Seiya ~New Bronze Cloth~ Golden Limited Edition ~HK Version~ EX\tPegasus Seiya\t¥600.00\t¥660.00\t\t\t8/8/2016\t\t\t\t\tOther Limited Edition\tMyth Cloth EX\tSaint Seiya\tBronze Saint V2\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tFALSE\tTRUE\tFALSE\tTRUE\tFALSE\tFALSE\tFALSE\t\tOnly in China: https://tamashiiweb.com/event/182\t\t922/EWuoQm"))
        .thenReturn(cf6);

    CharacterFigureEntity cfe1 = new CharacterFigureEntity();
    cfe1.setId("1000000001");
    cfe1.setOriginalName("Libra Dohko EX <Revival>");
    cfe1.setBaseName("Libra Dohko");
    cfe1.setIssuanceJPY(createIssuance("¥22,000", "8/30/2023", "9/1/2023", "2/2024"));
    cfe1.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe1.setFutureRelease(!StringUtils.hasText("2/2024"));
    cfe1.setUrl(toStringValue("https://tamashiiweb.com/item/14590"));
    cfe1.setDistribution(Distribution.STORES);
    cfe1.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe1.setSeries(Series.SAINT_SEIYA);
    cfe1.setGroup(Group.GOLD);
    cfe1.setMetal(false);
    cfe1.setOce(false);
    cfe1.setRevival(true);
    cfe1.setPlainCloth(false);
    cfe1.setBrokenCloth(false);
    cfe1.setGolden(false);
    cfe1.setGold(false);
    cfe1.setHk(false);
    cfe1.setManga(false);
    cfe1.setSurplice(false);
    cfe1.setSet(false);
    cfe1.setAnniversary(null);
    cfe1.setRemarks(null);
    cfe1.setTags(Set.of("ex", "dohko", "libra", "revival"));
    when(modelMapper.toEntity(cf1)).thenReturn(cfe1);

    CharacterFigureEntity cfe2 = new CharacterFigureEntity();
    cfe2.setId("1000000002");
    cfe2.setOriginalName("Pegasus Seiya ~Initial Bronze Cloth 20th Anniversary Ver.~");
    cfe2.setBaseName("Pegasus Seiya");
    cfe2.setIssuanceJPY(createIssuance("¥12,500", "9/7/2023", "9/8/2023", "1/2024"));
    cfe2.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe2.setFutureRelease(!StringUtils.hasText("1/2024"));
    cfe2.setUrl(toStringValue("https://tamashiiweb.com/item/14566"));
    cfe2.setDistribution(Distribution.TAMASHII_WEB_SHOP);
    cfe2.setLineUp(LineUp.MYTH_CLOTH);
    cfe2.setSeries(Series.SAINT_SEIYA);
    cfe2.setGroup(Group.V1);
    cfe2.setMetal(false);
    cfe2.setOce(false);
    cfe2.setRevival(false);
    cfe2.setPlainCloth(false);
    cfe2.setBrokenCloth(false);
    cfe2.setGolden(false);
    cfe2.setGold(false);
    cfe2.setHk(false);
    cfe2.setManga(false);
    cfe2.setSurplice(false);
    cfe2.setSet(false);
    cfe2.setAnniversary(toInteger("20"));
    cfe2.setRemarks("20th anniversary");
    cfe2.setTags(Set.of("seiya", "pegasus"));
    when(modelMapper.toEntity(cf2)).thenReturn(cfe2);

    CharacterFigureEntity cfe3 = new CharacterFigureEntity();
    cfe3.setId("1000000003");
    cfe3.setOriginalName("Goddess Athena & Saori Kido -Divine Saga Premium Set- EX");
    cfe3.setBaseName("Goddess Athena & Saori Kido ~Divine Saga Premium Set~");
    cfe3.setIssuanceJPY(createIssuance("¥32,000", "", "7/3/2023", "12/23/2024"));
    cfe3.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe3.setFutureRelease(!StringUtils.hasText("12/23/2023"));
    cfe3.setUrl(toStringValue("https://tamashiiweb.com/item/14469"));
    cfe3.setDistribution(Distribution.STORES);
    cfe3.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe3.setSeries(Series.SAINT_SEIYA);
    cfe3.setGroup(Group.GOD);
    cfe3.setMetal(true);
    cfe3.setOce(false);
    cfe3.setRevival(false);
    cfe3.setPlainCloth(false);
    cfe3.setBrokenCloth(false);
    cfe3.setGolden(false);
    cfe3.setGold(false);
    cfe3.setHk(false);
    cfe3.setManga(false);
    cfe3.setSurplice(false);
    cfe3.setSet(true);
    cfe3.setAnniversary(null);
    cfe3.setRemarks("20th anniversary");
    cfe3.setTags(
        Set.of(
            "premium", "ex", "set", "goddess", "&", "metal", "~divine", "set~", "athena", "saori",
            "kido", "saga"));
    when(modelMapper.toEntity(cf3)).thenReturn(cfe3);

    CharacterFigureEntity cfe4 = new CharacterFigureEntity();
    cfe4.setId("1000000004");
    cfe4.setOriginalName("Taurus Aldebaran (God Cloth) EX");
    cfe4.setBaseName("Taurus Aldebaran");
    cfe4.setIssuanceJPY(createIssuance("¥16,000", "", "5/7/2016", "11/26/2016"));
    cfe4.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe4.setFutureRelease(!StringUtils.hasText("11/26/2016"));
    cfe4.setUrl(toStringValue("https://tamashiiweb.com/item/11447"));
    cfe4.setDistribution(Distribution.STORES);
    cfe4.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe4.setSeries(Series.SOG);
    cfe4.setGroup(Group.GOLD);
    cfe4.setMetal(false);
    cfe4.setOce(false);
    cfe4.setRevival(false);
    cfe4.setPlainCloth(false);
    cfe4.setBrokenCloth(false);
    cfe4.setGolden(false);
    cfe4.setGold(false);
    cfe4.setHk(false);
    cfe4.setManga(false);
    cfe4.setSurplice(false);
    cfe4.setSet(false);
    cfe4.setAnniversary(null);
    cfe4.setRemarks(null);
    cfe4.setTags(Set.of("gold", "ex", "soul", "taurus", "aldebaran", "god"));
    when(modelMapper.toEntity(cf4)).thenReturn(cfe4);

    CharacterFigureEntity cfe5 = new CharacterFigureEntity();
    cfe5.setId("1000000005");
    cfe5.setOriginalName("Pegasus Seiya & Goddess Athena ~Broken~ OCE");
    cfe5.setBaseName("Pegasus Seiya & Goddess Athena ~Broken~");
    cfe5.setIssuanceJPY(createIssuance("¥7,500", "", "", "2/11/2011"));
    cfe5.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe5.setFutureRelease(!StringUtils.hasText("2/11/2011"));
    cfe5.setUrl(toStringValue(""));
    cfe5.setDistribution(Distribution.TAMASHII_NATIONS);
    cfe5.setLineUp(LineUp.MYTH_CLOTH);
    cfe5.setSeries(Series.SAINT_SEIYA);
    cfe5.setGroup(Group.OTHER);
    cfe5.setMetal(false);
    cfe5.setOce(true);
    cfe5.setRevival(false);
    cfe5.setPlainCloth(false);
    cfe5.setBrokenCloth(true);
    cfe5.setGolden(false);
    cfe5.setGold(false);
    cfe5.setHk(false);
    cfe5.setManga(false);
    cfe5.setSurplice(false);
    cfe5.setSet(false);
    cfe5.setAnniversary(null);
    cfe5.setRemarks("Tamashii Features 2011");
    cfe5.setTags(
        Set.of(
            "broken",
            "oce",
            "original",
            "color",
            "&",
            "goddess",
            "~broken~",
            "athena",
            "seiya",
            "pegasus"));
    when(modelMapper.toEntity(cf5)).thenReturn(cfe5);

    CharacterFigureEntity cfe6 = new CharacterFigureEntity();
    cfe6.setId("1000000006");
    cfe6.setOriginalName("Pegasus Seiya ~New Bronze Cloth~ Golden Limited Edition ~HK Version~ EX");
    cfe6.setBaseName("Pegasus Seiya");
    cfe6.setIssuanceJPY(createIssuance("¥600", "", "", "8/8/2016"));
    cfe6.setIssuanceMXN(createIssuance("", null, "", ""));
    cfe6.setFutureRelease(!StringUtils.hasText("8/8/2016"));
    cfe6.setUrl(toStringValue(""));
    cfe6.setDistribution(Distribution.OTHER);
    cfe6.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe6.setSeries(Series.SAINT_SEIYA);
    cfe6.setGroup(Group.V2);
    cfe6.setMetal(false);
    cfe6.setOce(false);
    cfe6.setRevival(false);
    cfe6.setPlainCloth(false);
    cfe6.setBrokenCloth(false);
    cfe6.setGolden(true);
    cfe6.setGold(false);
    cfe6.setHk(true);
    cfe6.setManga(false);
    cfe6.setSurplice(false);
    cfe6.setSet(false);
    cfe6.setAnniversary(null);
    cfe6.setRemarks("Only in China: https://tamashiiweb.com/event/182");
    cfe6.setTags(Set.of("ex", "asia", "golden", "seiya", "pegasus"));
    when(modelMapper.toEntity(cf6)).thenReturn(cfe6);

    // The service is tested here ...
    List<CharacterFigureEntity> entity =
        service.convertStreamToEntityList(new ByteArrayInputStream(bytes));
    assertNotNull(entity);
    assertEquals(6, entity.size());

    var i = 0;
    assertEquals("1000000006", entity.get(i).getId());
    assertEquals(
        "Pegasus Seiya ~New Bronze Cloth~ Golden Limited Edition ~HK Version~ EX",
        entity.get(i).getOriginalName());
    assertEquals("Pegasus Seiya", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("600"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceJPY().getPreorderDate());
    assertNull(entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2016, 8, 8), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertNull(entity.get(i).getUrl());
    assertEquals(Distribution.OTHER, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, entity.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, entity.get(i).getSeries());
    assertEquals(Group.V2, entity.get(i).getGroup());
    assertFalse(entity.get(i).isMetal());
    assertFalse(entity.get(i).isOce());
    assertFalse(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertFalse(entity.get(i).isBrokenCloth());
    assertTrue(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertTrue(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertFalse(entity.get(i).isSet());
    assertNull(entity.get(i).getAnniversary());
    assertEquals("Only in China: https://tamashiiweb.com/event/182", entity.get(i).getRemarks());
    assertEquals(entity.get(i).getTags(), Set.of("ex", "asia", "golden", "seiya", "pegasus"));

    i++;
    assertEquals("1000000005", entity.get(i).getId());
    assertEquals("Pegasus Seiya & Goddess Athena ~Broken~ OCE", entity.get(i).getOriginalName());
    assertEquals("Pegasus Seiya & Goddess Athena ~Broken~", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("7500"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceJPY().getPreorderDate());
    assertNull(entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2011, 2, 11), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertNull(entity.get(i).getUrl());
    assertEquals(Distribution.TAMASHII_NATIONS, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH, entity.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, entity.get(i).getSeries());
    assertEquals(Group.OTHER, entity.get(i).getGroup());
    assertFalse(entity.get(i).isMetal());
    assertTrue(entity.get(i).isOce());
    assertFalse(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertTrue(entity.get(i).isBrokenCloth());
    assertFalse(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertFalse(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertFalse(entity.get(i).isSet());
    assertNull(entity.get(i).getAnniversary());
    assertEquals("Tamashii Features 2011", entity.get(i).getRemarks());
    assertEquals(
        entity.get(i).getTags(),
        Set.of(
            "broken",
            "oce",
            "original",
            "color",
            "&",
            "goddess",
            "~broken~",
            "athena",
            "seiya",
            "pegasus"));

    i++;
    assertEquals("1000000004", entity.get(i).getId());
    assertEquals("Taurus Aldebaran (God Cloth) EX", entity.get(i).getOriginalName());
    assertEquals("Taurus Aldebaran", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("16000"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2016, 5, 7), entity.get(i).getIssuanceJPY().getPreorderDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2016, 11, 26), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/11447", entity.get(i).getUrl());
    assertEquals(Distribution.STORES, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, entity.get(i).getLineUp());
    assertEquals(Series.SOG, entity.get(i).getSeries());
    assertEquals(Group.GOLD, entity.get(i).getGroup());
    assertFalse(entity.get(i).isMetal());
    assertFalse(entity.get(i).isOce());
    assertFalse(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertFalse(entity.get(i).isBrokenCloth());
    assertFalse(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertFalse(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertFalse(entity.get(i).isSet());
    assertNull(entity.get(i).getAnniversary());
    assertNull(entity.get(i).getRemarks());
    assertEquals(
        entity.get(i).getTags(), Set.of("gold", "ex", "soul", "taurus", "aldebaran", "god"));

    i++;
    assertEquals("1000000003", entity.get(i).getId());
    assertEquals(
        "Goddess Athena & Saori Kido -Divine Saga Premium Set- EX",
        entity.get(i).getOriginalName());
    assertEquals(
        "Goddess Athena & Saori Kido ~Divine Saga Premium Set~", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("32000"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2023, 7, 3), entity.get(i).getIssuanceJPY().getPreorderDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2024, 12, 23), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/14469", entity.get(i).getUrl());
    assertEquals(Distribution.STORES, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, entity.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, entity.get(i).getSeries());
    assertEquals(Group.GOD, entity.get(i).getGroup());
    assertTrue(entity.get(i).isMetal());
    assertFalse(entity.get(i).isOce());
    assertFalse(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertFalse(entity.get(i).isBrokenCloth());
    assertFalse(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertFalse(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertTrue(entity.get(i).isSet());
    assertNull(entity.get(i).getAnniversary());
    assertEquals("20th anniversary", entity.get(i).getRemarks());
    assertEquals(
        entity.get(i).getTags(),
        Set.of(
            "goddess", "~divine", "premium", "set", "athena", "saori", "kido", "ex", "&", "metal",
            "saga", "set~"));

    i++;
    assertEquals("1000000002", entity.get(i).getId());
    assertEquals(
        "Pegasus Seiya ~Initial Bronze Cloth 20th Anniversary Ver.~",
        entity.get(i).getOriginalName());
    assertEquals("Pegasus Seiya", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("12500"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertEquals(
        LocalDate.of(2023, 9, 7), entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2023, 9, 8), entity.get(i).getIssuanceJPY().getPreorderDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2024, 1, 1), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(false, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/14566", entity.get(i).getUrl());
    assertEquals(Distribution.TAMASHII_WEB_SHOP, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH, entity.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, entity.get(i).getSeries());
    assertEquals(Group.V1, entity.get(i).getGroup());
    assertFalse(entity.get(i).isMetal());
    assertFalse(entity.get(i).isOce());
    assertFalse(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertFalse(entity.get(i).isBrokenCloth());
    assertFalse(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertFalse(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertFalse(entity.get(i).isSet());
    assertEquals(20, entity.get(i).getAnniversary());
    assertEquals("20th anniversary", entity.get(i).getRemarks());
    assertEquals(Set.of("seiya", "pegasus"), entity.get(i).getTags());

    i++;
    assertEquals("1000000001", entity.get(i).getId());
    assertEquals("Libra Dohko EX <Revival>", entity.get(i).getOriginalName());
    assertEquals("Libra Dohko", entity.get(i).getBaseName());
    assertNotNull(entity.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("22000"), entity.get(i).getIssuanceJPY().getBasePrice());
    assertNull(entity.get(i).getIssuanceJPY().getReleasePrice());
    assertEquals(
        LocalDate.of(2023, 8, 30), entity.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2023, 9, 1), entity.get(i).getIssuanceJPY().getPreorderDate());
    assertEquals(true, entity.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2024, 2, 1), entity.get(i).getIssuanceJPY().getReleaseDate());
    assertEquals(false, entity.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(entity.get(i).getIssuanceMXN());
    assertNull(entity.get(i).getIssuanceMXN().getBasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(entity.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(entity.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(entity.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(entity.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/14590", entity.get(i).getUrl());
    assertEquals(Distribution.STORES, entity.get(i).getDistribution());
    assertEquals(LineUp.MYTH_CLOTH_EX, entity.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, entity.get(i).getSeries());
    assertEquals(Group.GOLD, entity.get(i).getGroup());
    assertFalse(entity.get(i).isMetal());
    assertFalse(entity.get(i).isOce());
    assertTrue(entity.get(i).isRevival());
    assertFalse(entity.get(i).isPlainCloth());
    assertFalse(entity.get(i).isBrokenCloth());
    assertFalse(entity.get(i).isGolden());
    assertFalse(entity.get(i).isGold());
    assertFalse(entity.get(i).isHk());
    assertFalse(entity.get(i).isManga());
    assertFalse(entity.get(i).isSurplice());
    assertFalse(entity.get(i).isSet());
    assertNull(entity.get(i).getAnniversary());
    assertNull(entity.get(i).getRemarks());
    assertEquals(Set.of("revival", "dohko", "ex", "libra"), entity.get(i).getTags());

    // tests the updated tags.
    assertEquals(Set.of("ex", "dohko", "libra", "revival"), cf1.getTags());
    assertEquals(Set.of("seiya", "pegasus"), cf2.getTags());
    assertEquals(
        Set.of(
            "premium", "ex", "set", "goddess", "&", "metal", "~divine", "set~", "athena", "saori",
            "kido", "saga"),
        cf3.getTags());
    assertEquals(Set.of("gold", "ex", "soul", "taurus", "aldebaran", "god"), cf4.getTags());
    assertEquals(
        Set.of(
            "broken",
            "oce",
            "original",
            "color",
            "&",
            "goddess",
            "~broken~",
            "athena",
            "seiya",
            "pegasus"),
        cf5.getTags());
    assertEquals(Set.of("ex", "asia", "golden", "seiya", "pegasus"), cf6.getTags());
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Initial Bronze Cloth~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV2MythCloth_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V2);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV2MythClothEX_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V2);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~New Bronze Cloth~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV3_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V3);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Final Bronze Cloth~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV4_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V4);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (God Cloth)", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV5_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V5);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Heaven Chapter)", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupOtherPlainCloth_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.OTHER);
    characterFigure.setPlainCloth(true);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Plain Clothes)", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1MythClothGold_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setBronzeToGold(true);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Limited Gold~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV2MythClothGold_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V2);
    characterFigure.setBronzeToGold(true);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Power of Gold~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV2MythClothEXGold_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V2);
    characterFigure.setBronzeToGold(true);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (New Bronze Cloth) ~Golden Limited Edition~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV3MythClothEXGold_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V3);
    characterFigure.setBronzeToGold(true);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Final Bronze Cloth) ~Golden Limited Edition~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV4MythClothEXGold_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V4);
    characterFigure.setBronzeToGold(true);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (God Cloth)", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1Manga_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setManga(true);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Initial Bronze Cloth~ ~Comic Version~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1OCE_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setOce(true);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~Original Color Edition~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1Anniversary_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setAnniversary(30);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya (Initial Bronze Cloth) ~30th Anniversary Ver.~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1HK_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setHongKongVersion(true);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Initial Bronze Cloth~ ~HK Version~", displayableName);
  }

  /** Test for {@link CharacterFigureService#calculateFigureDisplayableName(CharacterFigure)} */
  @Test
  public void calculateFigureDisplayableName_whenGroupV1Surplice_thenSuccess() {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setBaseName("Pegasus Seiya");
    characterFigure.setGroup(Group.V1);
    characterFigure.setSurplice(true);

    // The service is tested here ...
    String displayableName = service.calculateFigureDisplayableName(characterFigure);
    assertEquals("Pegasus Seiya ~Initial Bronze Cloth~ (Surplice)", displayableName);
  }

  /** Test for {@link CharacterFigureService#retrieveAllCharacters(RestockType, String)} */
  @Test
  public void retrieveAllCharacters_whenAllCharactersAvailable_thenSuccess() {
    CharacterFigureEntity cfe1 = createCharacterFigureEntityWithReleaseDate("1000000001", "7/2024");
    CharacterFigureEntity cfe2 = createCharacterFigureEntityWithReleaseDate("1000000002", "3/2018");
    when(repo.findAll(getSorting())).thenReturn(List.of(cfe1, cfe2));

    when(modelMapper.toModel(cfe1)).thenReturn(createCharacterFigureWithReleaseDate("7/2024"));
    when(modelMapper.toModel(cfe2)).thenReturn(createCharacterFigureWithReleaseDate("3/2018"));

    // The service is tested here ...
    List<CharacterFigure> characters = service.retrieveAllCharacters(RestockType.ALL, null);
    assertNotNull(characters);
    assertEquals(2, characters.size());

    var i = 0;
    assertNull(characters.get(i).getId());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~ EX", characters.get(i).getOriginalName());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~", characters.get(i).getBaseName());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~", characters.get(i).getDisplayableName());
    assertEquals(LineUp.MYTH_CLOTH_EX, characters.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, characters.get(i).getSeries());
    assertEquals(Group.GOLD, characters.get(i).getGroup());
    assertTrue(characters.get(i).isMetalBody());
    assertFalse(characters.get(i).isOce());
    assertFalse(characters.get(i).isRevival());
    assertFalse(characters.get(i).isPlainCloth());
    assertFalse(characters.get(i).isBrokenCloth());
    assertFalse(characters.get(i).isBronzeToGold());
    assertFalse(characters.get(i).isGold());
    assertFalse(characters.get(i).isHongKongVersion());
    assertFalse(characters.get(i).isManga());
    assertFalse(characters.get(i).isSurplice());
    assertFalse(characters.get(i).isSet());
    assertNull(characters.get(i).getAnniversary());
    assertNull(characters.get(i).getTags());
    assertNotNull(characters.get(i).getImages());
    assertEquals(1, characters.get(i).getImages().size());
    assertNull(characters.get(i).getImages().get(0).getIdImage());
    assertEquals("924/b2TERq", characters.get(i).getImages().get(0).getUrl());
    assertTrue(characters.get(i).getImages().get(0).isOfficial());
    assertFalse(characters.get(i).getImages().get(0).isCoverPhoto());
    assertEquals(0, characters.get(i).getImages().get(0).getOrder());
    assertNotNull(characters.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("20000"), characters.get(i).getIssuanceJPY().getBasePrice());
    assertEquals(new BigDecimal("22000.00"), characters.get(i).getIssuanceJPY().getReleasePrice());
    assertEquals(
        LocalDate.of(2023, 12, 28), characters.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 1, 10), characters.get(i).getIssuanceJPY().getPreorderDate());
    assertTrue(characters.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2024, 7, 1), characters.get(i).getIssuanceJPY().getReleaseDate());
    assertFalse(characters.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(characters.get(i).getIssuanceMXN());
    assertEquals(new BigDecimal("3800"), characters.get(i).getIssuanceMXN().getBasePrice());
    assertEquals(new BigDecimal("3800"), characters.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(characters.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(characters.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(characters.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(characters.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(characters.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(characters.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/14738", characters.get(i).getUrl());
    assertEquals(Distribution.STORES, characters.get(i).getDistribution());
    assertNull(characters.get(i).getRemarks());

    i++;
    assertNull(characters.get(i).getId());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~ EX", characters.get(i).getOriginalName());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~", characters.get(i).getBaseName());
    assertEquals(
        "Sagittarius Seiya ~Inheritor of the Gold Cloth~", characters.get(i).getDisplayableName());
    assertEquals(LineUp.MYTH_CLOTH_EX, characters.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, characters.get(i).getSeries());
    assertEquals(Group.GOLD, characters.get(i).getGroup());
    assertTrue(characters.get(i).isMetalBody());
    assertFalse(characters.get(i).isOce());
    assertFalse(characters.get(i).isRevival());
    assertFalse(characters.get(i).isPlainCloth());
    assertFalse(characters.get(i).isBrokenCloth());
    assertFalse(characters.get(i).isBronzeToGold());
    assertFalse(characters.get(i).isGold());
    assertFalse(characters.get(i).isHongKongVersion());
    assertFalse(characters.get(i).isManga());
    assertFalse(characters.get(i).isSurplice());
    assertFalse(characters.get(i).isSet());
    assertNull(characters.get(i).getAnniversary());
    assertNull(characters.get(i).getTags());
    assertNotNull(characters.get(i).getImages());
    assertEquals(1, characters.get(i).getImages().size());
    assertNull(characters.get(i).getImages().get(0).getIdImage());
    assertEquals("924/b2TERq", characters.get(i).getImages().get(0).getUrl());
    assertTrue(characters.get(i).getImages().get(0).isOfficial());
    assertFalse(characters.get(i).getImages().get(0).isCoverPhoto());
    assertEquals(0, characters.get(i).getImages().get(0).getOrder());
    assertNotNull(characters.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("20000"), characters.get(i).getIssuanceJPY().getBasePrice());
    assertEquals(new BigDecimal("21600.00"), characters.get(i).getIssuanceJPY().getReleasePrice());
    assertEquals(
        LocalDate.of(2023, 12, 28), characters.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertEquals(LocalDate.of(2024, 1, 10), characters.get(i).getIssuanceJPY().getPreorderDate());
    assertTrue(characters.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2018, 3, 1), characters.get(i).getIssuanceJPY().getReleaseDate());
    assertFalse(characters.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNotNull(characters.get(i).getIssuanceMXN());
    assertEquals(new BigDecimal("3800"), characters.get(i).getIssuanceMXN().getBasePrice());
    assertEquals(new BigDecimal("3800"), characters.get(i).getIssuanceMXN().getReleasePrice());
    assertNull(characters.get(i).getIssuanceMXN().getFirstAnnouncementDate());
    assertNull(characters.get(i).getIssuanceMXN().getPreorderDate());
    assertNull(characters.get(i).getIssuanceMXN().getPreorderConfirmationDay());
    assertNull(characters.get(i).getIssuanceMXN().getReleaseDate());
    assertNull(characters.get(i).getIssuanceMXN().getReleaseConfirmationDay());
    assertFalse(characters.get(i).isFutureRelease());
    assertEquals("https://tamashiiweb.com/item/14738", characters.get(i).getUrl());
    assertEquals(Distribution.STORES, characters.get(i).getDistribution());
    assertNull(characters.get(i).getRemarks());
  }

  /** Test for {@link CharacterFigureService#retrieveAllCharacters(RestockType, String)} */
  @Test
  public void retrieveAllCharacters_whenAllCharactersAvailable_thenReturnAll() {
    CharacterFigureEntity entity1 =
        createBasicSSEXFigureEntity("12345", "Pegasus Seiya", "Pegasus Seiya", Group.GOD, false);
    List<CharacterFigureEntity> mockList = new ArrayList<>();
    mockList.add(entity1);
    when(repo.findAll(getSorting())).thenReturn(mockList);

    CharacterFigure figure1 =
        createBasicFigure(
            null,
            "Pegasus Seiya",
            LocalDate.of(2018, 1, 27),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOD,
            false,
            false);
    when(modelMapper.toModel(entity1)).thenReturn(figure1);

    List<CharacterFigure> characters = service.retrieveAllCharacters(RestockType.ALL, null);
    assertNotNull(characters);
    assertFalse(characters.isEmpty());

    var i = 0;
    assertNull(characters.get(i).getId());
    assertEquals("Pegasus Seiya", characters.get(i).getOriginalName());
    assertEquals("Pegasus Seiya", characters.get(i).getBaseName());
    assertEquals("Pegasus Seiya", characters.get(i).getDisplayableName());
    assertEquals(LineUp.MYTH_CLOTH_EX, characters.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, characters.get(i).getSeries());
    assertEquals(Group.GOD, characters.get(i).getGroup());
    assertFalse(characters.get(i).isMetalBody());
    assertFalse(characters.get(i).isOce());
    assertFalse(characters.get(i).isRevival());
    assertFalse(characters.get(i).isPlainCloth());
    assertFalse(characters.get(i).isBrokenCloth());
    assertFalse(characters.get(i).isBronzeToGold());
    assertFalse(characters.get(i).isGold());
    assertFalse(characters.get(i).isHongKongVersion());
    assertFalse(characters.get(i).isManga());
    assertFalse(characters.get(i).isSurplice());
    assertFalse(characters.get(i).isSet());
    assertNull(characters.get(i).getAnniversary());
    assertNull(characters.get(i).getTags());
    assertNull(characters.get(i).getImages());
    assertNotNull(characters.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("12000"), characters.get(i).getIssuanceJPY().getBasePrice());
    assertEquals(new BigDecimal("12960.00"), characters.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(characters.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2018, 1, 27), characters.get(i).getIssuanceJPY().getReleaseDate());
    assertTrue(characters.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNull(characters.get(i).getIssuanceMXN());
    assertFalse(characters.get(i).isFutureRelease());
    assertNull(characters.get(i).getUrl());
    assertNull(characters.get(i).getDistribution());
    assertNull(characters.get(i).getRemarks());
  }

  /**
   * Test for
   * {@link CharacterFigureService#retrieveAllCharacters(RestockType, String)
   */
  @Test
  public void retrieveAllCharacters_whenAllCharactersAvailable_thenReturnNoRestocks() {
    CharacterFigureEntity entity =
        createBasicSSEXFigureEntity(
            "67890", "Libra Dohko (God Cloth) EX", "Libra Dohko", Group.GOLD, false);
    CharacterFigureEntity entityRestock =
        createBasicSSEXFigureEntity(
            "67891", "Libra Dohko (God Cloth) EX", "Libra Dohko", Group.GOLD, false);
    when(repo.findAll(getSorting())).thenReturn(List.of(entity, entityRestock));

    CharacterFigure figure =
        createBasicFigure(
            null,
            "Libra Dohko (God Cloth) EX",
            LocalDate.of(2018, 1, 27),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    CharacterFigure figureRestock =
        createBasicFigure(
            null,
            "Libra Dohko (God Cloth) EX",
            LocalDate.of(2021, 9, 18),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    when(modelMapper.toModel(entity)).thenReturn(figure);
    when(modelMapper.toModel(entityRestock)).thenReturn(figureRestock);

    List<CharacterFigure> characters = service.retrieveAllCharacters(RestockType.NONE, null);
    var i = 0;
    assertNotNull(characters);
    assertEquals(1, characters.size());
    assertNull(characters.get(i).getId());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getOriginalName());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getBaseName());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getDisplayableName());
    assertEquals(LineUp.MYTH_CLOTH_EX, characters.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, characters.get(i).getSeries());
    assertEquals(Group.GOLD, characters.get(i).getGroup());
    assertFalse(characters.get(i).isMetalBody());
    assertFalse(characters.get(i).isOce());
    assertFalse(characters.get(i).isRevival());
    assertFalse(characters.get(i).isPlainCloth());
    assertFalse(characters.get(i).isBrokenCloth());
    assertFalse(characters.get(i).isBronzeToGold());
    assertFalse(characters.get(i).isGold());
    assertFalse(characters.get(i).isHongKongVersion());
    assertFalse(characters.get(i).isManga());
    assertFalse(characters.get(i).isSurplice());
    assertFalse(characters.get(i).isSet());
    assertNull(characters.get(i).getAnniversary());
    assertNull(characters.get(i).getTags());
    assertNull(characters.get(i).getImages());
    assertNotNull(characters.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("12000"), characters.get(i).getIssuanceJPY().getBasePrice());
    assertEquals(new BigDecimal("12960.00"), characters.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(characters.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2018, 1, 27), characters.get(i).getIssuanceJPY().getReleaseDate());
    assertTrue(characters.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNull(characters.get(i).getIssuanceMXN());
    assertFalse(characters.get(i).isFutureRelease());
    assertNull(characters.get(i).getUrl());
    assertNull(characters.get(i).getDistribution());
    assertNull(characters.get(i).getRemarks());
  }

  /**
   * Test for
   * {@link CharacterFigureService#retrieveAllCharacters(RestockType, String)
   */
  @Test
  public void retrieveAllCharacters_whenAllCharactersAvailable_thenReturnOnlyRestocks() {
    CharacterFigureEntity entity =
        createBasicSSEXFigureEntity(
            "67890", "Libra Dohko (God Cloth) EX", "Libra Dohko", Group.GOLD, false);
    CharacterFigureEntity entityRestock =
        createBasicSSEXFigureEntity(
            "67891", "Libra Dohko (God Cloth) EX", "Libra Dohko", Group.GOLD, false);
    when(repo.findAll(getSorting())).thenReturn(List.of(entity, entityRestock));

    CharacterFigure figure =
        createBasicFigure(
            null,
            "Libra Dohko (God Cloth) EX",
            LocalDate.of(2018, 1, 27),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    CharacterFigure figureRestock =
        createBasicFigure(
            null,
            "Libra Dohko (God Cloth) EX",
            LocalDate.of(2021, 9, 18),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    when(modelMapper.toModel(entity)).thenReturn(figure);
    when(modelMapper.toModel(entityRestock)).thenReturn(figureRestock);

    List<CharacterFigure> characters = service.retrieveAllCharacters(RestockType.ONLY, null);
    var i = 0;
    assertNotNull(characters);
    assertEquals(1, characters.size());
    assertNull(characters.get(i).getId());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getOriginalName());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getBaseName());
    assertEquals("Libra Dohko (God Cloth) EX", characters.get(i).getDisplayableName());
    assertEquals(LineUp.MYTH_CLOTH_EX, characters.get(i).getLineUp());
    assertEquals(Series.SAINT_SEIYA, characters.get(i).getSeries());
    assertEquals(Group.GOLD, characters.get(i).getGroup());
    assertFalse(characters.get(i).isMetalBody());
    assertFalse(characters.get(i).isOce());
    assertFalse(characters.get(i).isRevival());
    assertFalse(characters.get(i).isPlainCloth());
    assertFalse(characters.get(i).isBrokenCloth());
    assertFalse(characters.get(i).isBronzeToGold());
    assertFalse(characters.get(i).isGold());
    assertFalse(characters.get(i).isHongKongVersion());
    assertFalse(characters.get(i).isManga());
    assertFalse(characters.get(i).isSurplice());
    assertFalse(characters.get(i).isSet());
    assertNull(characters.get(i).getAnniversary());
    assertNull(characters.get(i).getTags());
    assertNull(characters.get(i).getImages());
    assertNotNull(characters.get(i).getIssuanceJPY());
    assertEquals(new BigDecimal("12000"), characters.get(i).getIssuanceJPY().getBasePrice());
    assertEquals(new BigDecimal("13200.00"), characters.get(i).getIssuanceJPY().getReleasePrice());
    assertNull(characters.get(i).getIssuanceJPY().getFirstAnnouncementDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderDate());
    assertNull(characters.get(i).getIssuanceJPY().getPreorderConfirmationDay());
    assertEquals(LocalDate.of(2021, 9, 18), characters.get(i).getIssuanceJPY().getReleaseDate());
    assertTrue(characters.get(i).getIssuanceJPY().getReleaseConfirmationDay());
    assertNull(characters.get(i).getIssuanceMXN());
    assertFalse(characters.get(i).isFutureRelease());
    assertNull(characters.get(i).getUrl());
    assertNull(characters.get(i).getDistribution());
    assertNull(characters.get(i).getRemarks());
  }

  /** Test for {@link CharacterFigureService#retrieveAllCharacters(RestockType, String) */
  @Test
  public void retrieveAllCharacters_whenAllCharactersAvailable_thenReturnOnlyRestocks_() {
    CharacterFigureEntity entity1 =
        createBasicSSEXFigureEntity(
            "67890", "Libra Dohko (God Cloth) EX", "Libra Dohko", Group.GOLD, false);
    CharacterFigureEntity entity2 =
        createBasicSSEXFigureEntity(
            "67891", "Gemini Saga (Surplice) EX", "Gemini Saga", Group.SURPLICE, false);
    CharacterFigureEntity entity3 =
        createBasicSSEXFigureEntity(
            "67892", "Gemini Saga Gold 24K EX", "Gemini Saga", Group.GOLD, false);
    CharacterFigureEntity entity4 =
        createBasicSSEXFigureEntity(
            "67893", "Gemini Saga EX <Revival>", "Gemini Saga", Group.GOLD, true);

    when(repo.findAll(getSorting())).thenReturn(List.of(entity1, entity2, entity3, entity4));

    CharacterFigure figure1 =
        createBasicFigure(
            null,
            "Libra Dohko (God Cloth) EX",
            LocalDate.of(2018, 1, 27),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    CharacterFigure figure2 =
        createBasicFigure(
            null,
            "Gemini Saga (Surplice) EX",
            LocalDate.of(2021, 9, 18),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.SURPLICE,
            false,
            false);
    CharacterFigure figure3 =
        createBasicFigure(
            null,
            "Gemini Saga Gold 24K EX",
            LocalDate.of(2021, 9, 18),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            false);
    CharacterFigure figure4 =
        createBasicFigure(
            null,
            "Gemini Saga EX <Revival>",
            LocalDate.of(2021, 9, 18),
            LineUp.MYTH_CLOTH_EX,
            Series.SAINT_SEIYA,
            Group.GOLD,
            false,
            true);
    when(modelMapper.toModel(entity1)).thenReturn(figure1);
    when(modelMapper.toModel(entity2)).thenReturn(figure2);
    when(modelMapper.toModel(entity3)).thenReturn(figure3);
    when(modelMapper.toModel(entity4)).thenReturn(figure4);

    when(props.namingExclusions()).thenReturn(getNamingExclusions());

    List<CharacterFigure> characters =
        service.retrieveAllCharacters(
            RestockType.ALL,
            "Bandai Saint Seiya Myth Cloth EX Gemini Saga (God Cloth) / no correction BOX - Saga Saga premium set - no correction");
  }

  private CharacterFigure createCharacterFigureWithReleaseDate(String releaseDateJPY) {
    CharacterFigure cf = new CharacterFigure();
    cf.setOriginalName("Sagittarius Seiya ~Inheritor of the Gold Cloth~ EX");
    cf.setBaseName("Sagittarius Seiya ~Inheritor of the Gold Cloth~");
    cf.setIssuanceJPY(createIssuance("¥20,000", "12/28/2023", "1/10/2024", releaseDateJPY));
    cf.setIssuanceMXN(createIssuance("$3,800", null, "", ""));
    cf.setFutureRelease(!StringUtils.hasText("7/2024"));
    cf.setUrl(toStringValue("https://tamashiiweb.com/item/14738"));
    cf.setDistribution(Distribution.STORES);
    cf.setLineUp(LineUp.MYTH_CLOTH_EX);
    cf.setSeries(Series.SAINT_SEIYA);
    cf.setGroup(Group.GOLD);
    cf.setMetalBody(true);
    cf.setOce(false);
    cf.setRevival(false);
    cf.setPlainCloth(false);
    cf.setBrokenCloth(false);
    cf.setBronzeToGold(false);
    cf.setGold(false);
    cf.setHongKongVersion(false);
    cf.setManga(false);
    cf.setSurplice(false);
    cf.setSet(false);
    cf.setAnniversary(null);
    cf.setRemarks(null);
    cf.setTags(null);
    cf.setImages(List.of(new GalleryImage(null, "924/b2TERq", true, false, 0)));
    return cf;
  }

  private CharacterFigureEntity createCharacterFigureEntityWithReleaseDate(
      String id, String releaseDateJPY) {
    CharacterFigureEntity cfe = new CharacterFigureEntity();
    cfe.setId(id);
    cfe.setOriginalName("Sagittarius Seiya ~Inheritor of the Gold Cloth~ EX");
    cfe.setBaseName("Sagittarius Seiya ~Inheritor of the Gold Cloth~");
    cfe.setIssuanceJPY(createIssuance("¥20,000", "12/28/2023", "1/10/2024", releaseDateJPY));
    cfe.setIssuanceMXN(createIssuance("$3,800", null, "", ""));
    cfe.setFutureRelease(!StringUtils.hasText("7/2024"));
    cfe.setUrl(toStringValue("https://tamashiiweb.com/item/14738"));
    cfe.setDistribution(Distribution.STORES);
    cfe.setLineUp(LineUp.MYTH_CLOTH_EX);
    cfe.setSeries(Series.SAINT_SEIYA);
    cfe.setGroup(Group.GOLD);
    cfe.setMetal(true);
    cfe.setOce(false);
    cfe.setRevival(false);
    cfe.setPlainCloth(false);
    cfe.setBrokenCloth(false);
    cfe.setGolden(false);
    cfe.setGold(false);
    cfe.setHk(false);
    cfe.setManga(false);
    cfe.setSurplice(false);
    cfe.setSet(false);
    cfe.setAnniversary(null);
    cfe.setRemarks(null);
    cfe.setTags(null);
    cfe.setImages(List.of(new GalleryImage(null, "924/b2TERq", true, false, 0)));
    return cfe;
  }

  /**
   * This method is used to create a very generic {@link CharacterFigureEntity}
   *
   * @param id The unique identifier.
   * @param originalName The original name.
   * @param baseName The base name.
   * @param group The group.
   * @param revival Is it revival?
   * @return The new {@link CharacterFigureEntity}
   */
  private CharacterFigureEntity createBasicSSEXFigureEntity(
      String id, String originalName, String baseName, Group group, boolean revival) {
    CharacterFigureEntity characterFigure = new CharacterFigureEntity();
    characterFigure.setId(id);
    characterFigure.setOriginalName(originalName);
    characterFigure.setBaseName(baseName);
    characterFigure.setLineUp(LineUp.MYTH_CLOTH_EX);
    characterFigure.setSeries(Series.SAINT_SEIYA);
    characterFigure.setGroup(group);
    characterFigure.setMetal(false);
    characterFigure.setOce(false);
    characterFigure.setRevival(revival);
    characterFigure.setPlainCloth(false);
    characterFigure.setHk(false);
    characterFigure.setManga(false);
    characterFigure.setSurplice(false);

    return characterFigure;
  }

  /**
   * Creates a basic figure.
   *
   * @param id The optional identifier.
   * @param originalName The original name.
   * @param releaseDate The release date.
   * @param lineUp The line-up.
   * @param series The series.
   * @param group The actual group.
   * @param oce Is it OCE?
   * @param revival is it revival?
   * @return The figure.
   */
  private CharacterFigure createBasicFigure(
      String id,
      String originalName,
      LocalDate releaseDate,
      LineUp lineUp,
      Series series,
      Group group,
      boolean oce,
      boolean revival) {
    CharacterFigure characterFigure = new CharacterFigure();
    characterFigure.setId(id);
    characterFigure.setOriginalName(originalName);
    characterFigure.setBaseName(originalName);
    Issuance issuanceJPY = new Issuance();
    issuanceJPY.setReleaseConfirmationDay(true);
    issuanceJPY.setReleaseDate(releaseDate);
    issuanceJPY.setBasePrice(new BigDecimal("12000"));
    characterFigure.setIssuanceJPY(issuanceJPY);
    characterFigure.setLineUp(lineUp);
    characterFigure.setSeries(series);
    characterFigure.setGroup(group);
    characterFigure.setOce(oce);
    characterFigure.setRevival(revival);

    return characterFigure;
  }

  private Issuance createIssuance(
      String price, String announcement, String preorder, String release) {
    Issuance issuance = new Issuance();
    issuance.setBasePrice(toPrice(price));
    issuance.setFirstAnnouncementDate(toDate(announcement));
    issuance.setPreorderDate(toDate(preorder));
    issuance.setPreorderConfirmationDay(isDayMonthYear(preorder));
    issuance.setReleaseDate(toDate(release));
    issuance.setReleaseConfirmationDay(isDayMonthYear(release));
    return issuance;
  }

  private Sort getSorting() {
    return Sort.by(
        List.of(
            new Sort.Order(Sort.Direction.DESC, "futureRelease"),
            new Sort.Order(Sort.Direction.DESC, "issuanceJPY.releaseDate")));
  }
}
