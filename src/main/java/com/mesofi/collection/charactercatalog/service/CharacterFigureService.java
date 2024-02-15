/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import static com.mesofi.collection.charactercatalog.model.RestockType.ALL;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.reverseListElements;

import com.mesofi.collection.charactercatalog.CharacterCatalogConfig;
import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.exception.CharacterFigureException;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureFileMapper;
import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Figure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockType;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.repository.CharacterFigureRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles the business logic of the service.
 *
 * @author armandorivasarzaluz
 */
@Slf4j
@Service
@AllArgsConstructor
public class CharacterFigureService {

  public static final String INVALID_BASE_NAME = "Provide a non empty base name";
  public static final String INVALID_GROUP = "Provide a valid group";
  public static final String INVALID_ID = "Provide a non empty character id";
  public static final String INVALID_IMAGE_ID = "Provide a non empty image id";
  public static final String INVALID_IMAGE_URL = "Provide a non empty url for the image";
  public static final String INVALID_ORDER_NUMBER = "Provide positive value fo the order";

  public static final String DEFAULT_JPG_EXT = ".jpg";
  public static final String DEFAULT_PNG_EXT = ".png";
  public static final String HOST_IMAGE_SIZE = "320x240";
  public static final String HOST_IMAGE_PREFIX =
      "https://imagizer.imageshack.com/v2/" + HOST_IMAGE_SIZE + "q70/";
  public static final String NO_IMAGE_URL = HOST_IMAGE_PREFIX + "923/3hbcya.png";

  public static final List<String> TAG_EX = List.of("ex");
  public static final List<String> TAG_SOG = List.of("soul", "gold", "god");
  public static final List<String> TAG_REVIVAL = List.of("revival");
  public static final List<String> TAG_SET = List.of("set");
  public static final List<String> TAG_BROKEN = List.of("broken");
  public static final List<String> TAG_METAL = List.of("metal");
  public static final List<String> TAG_OCE = List.of("oce", "original", "color");
  public static final List<String> TAG_HK = List.of("asia");
  public static final List<String> TAG_BRONZE_TO_GOLD = List.of("golden");

  private CharacterFigureRepository repo;
  private CharacterFigureModelMapper modelMapper;
  private CharacterFigureFileMapper fileMapper;
  private CharacterCatalogConfig.Props props;

  /**
   * Loads all the characters.
   *
   * @param file The reference to the file with all the records.
   * @return The total of records loaded.
   */
  public long loadAllCharacters(final MultipartFile file) {
    log.debug("Loading all the records ...");
    if (Objects.isNull(file)) {
      throw new IllegalArgumentException("The uploaded file is missing...");
    }
    InputStream inputStream;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      log.error("Can't read input file", e);
      throw new CharacterFigureException("Unable to read characters from initial input file");
    }

    // the records are read and processed now.
    List<CharacterFigureEntity> listEntities = convertStreamToEntityList(inputStream);

    // first, removes all the records.
    repo.deleteAll();

    // performs a save operation in the DB ...
    long total = repo.saveAll(listEntities).size();

    log.info("Total of figures loaded correctly: {}", total);
    return total;
  }

  /**
   * @param characterFigure
   * @return
   */
  public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
    return null;
  }

  /**
   * Converts and process the incoming records and return a list with the characters ready to be
   * saved in a persistence storage.
   *
   * @param inputStream Reference to the records read from a source.
   * @return The list or records.
   */
  public List<CharacterFigureEntity> convertStreamToEntityList(@NonNull InputStream inputStream) {
    // @formatter:off
    List<CharacterFigure> allCharacters =
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
            .lines()
            .skip(1) // we don't consider the header
            .map($ -> fileMapper.fromLineToCharacterFigure($))
            .collect(Collectors.toList());
    // @formatter:on

    // reverse the list so that we can add re-stocks easily ...
    reverseListElements(allCharacters);

    // gets the new characters with restocks
    log.debug("Total of figures to be loaded: {}", allCharacters.size());

    // add some tags
    allCharacters.forEach(this::addStandardTags);

    return allCharacters.stream().map($ -> modelMapper.toEntity($)).collect(Collectors.toList());
  }

  /**
   * Retrieve all the characters ordered by release date.
   *
   * @param type The restocking type.
   * @param name The name of the character.
   * @return The list of characters based on the restocking type.
   */
  public List<CharacterFigure> retrieveAllCharacters(final RestockType type, final String name) {
    Stream<CharacterFigure> stream =
        repo.findAll(getSorting()).stream().map(this::fromEntityToDisplayableFigure);
    List<CharacterFigure> list =
        switch (Optional.ofNullable(type).orElse(ALL)) {
          case ALL -> stream.toList();
          case NONE -> stream.distinct().toList();
          case ONLY -> findOnlyRestocks(stream.toList());
        };

    if (StringUtils.hasText(name)) {
      Predicate<String> wordNamePredicate =
          wordName -> props.namingExclusions().stream().noneMatch(wordName::equalsIgnoreCase);
      Set<String> simpleNameKeywords =
          Arrays.stream(name.split("\\s+"))
              .map(String::toLowerCase)
              .filter(wordNamePredicate)
              // removes characters for example: [], (), - etc ...
              .map(wordName -> wordName.replaceAll("[\\[\\]\"()-]", ""))
              .filter(wordNamePredicate)
              .collect(Collectors.toSet());

      log.info("Simplified figure name: {}", simpleNameKeywords);

      List<CharacterFigure> tmpList;
      for (String nameKeyword : simpleNameKeywords) {
        tmpList =
            list.stream()
                .filter($ -> $.getTags().stream().anyMatch(nameKeyword::equalsIgnoreCase))
                .toList();
        if (tmpList.isEmpty()) {
          continue; // no matches yet, we continue with the next tag
        }
        list = tmpList;
        if (list.size() == 1) {
          break; // we found at least one match
        }
      }
      log.info(
          "Character matches: {}",
          list.stream().map(CharacterFigure::getBaseName).collect(Collectors.toList()));
    }
    return list;
  }

  /**
   * This method converts an entity object to the corresponding model, after that, the price and
   * final name are calculated to be displayed. This is a convenient method to be called from other
   * services.
   *
   * @param entity The raw entity.
   * @return The figure model with the name and price calculated.
   */
  public CharacterFigure fromEntityToDisplayableFigure(CharacterFigureEntity entity) {
    CharacterFigure cf = modelMapper.toModel(entity);
    calculatePriceAndDisplayableName(cf);
    return cf;
  }

  /**
   * Calculate the figure name.
   *
   * @param figure The figure name object.
   * @return The displayable name.
   */
  public String calculateFigureDisplayableName(final CharacterFigure figure) {
    StringBuilder sb = new StringBuilder();
    sb.append(figure.getBaseName());

    switch (figure.getGroup()) {
      case V1:
        appendAttr(sb, "~Initial Bronze Cloth~");
        break;
      case V2:
        if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
          appendAttr(sb, "~New Bronze Cloth~");
        }
        break;
      case V3:
        appendAttr(sb, "~Final Bronze Cloth~");
        break;
      case V4:
        appendAttr(sb, "(God Cloth)");
        break;
      case V5:
        appendAttr(sb, "(Heaven Chapter)");
        break;
      default:
        break;
    }
    if (figure.isPlainCloth()) {
      appendAttr(sb, "(Plain Clothes)");
    }

    if (figure.isBronzeToGold()) {
      if (figure.getLineUp() == LineUp.MYTH_CLOTH) {
        if (figure.getGroup() == Group.V1) {
          sb = replacePattern(sb.toString());
          appendAttr(sb, "~Limited Gold~");
        }
        if (figure.getGroup() == Group.V2) {
          appendAttr(sb, "~Power of Gold~");
        }
      }
      if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
        if (figure.getGroup() == Group.V2 || figure.getGroup() == Group.V3) {
          sb = replacePattern(sb.toString());
          appendAttr(sb, "~Golden Limited Edition~");
        }
      }
    }

    if (figure.isManga()) {
      appendAttr(sb, "~Comic Version~");
    }
    if (figure.isOce()) {
      sb = replacePattern(sb.toString());
      appendAttr(sb, "~Original Color Edition~");
    }
    if (Objects.nonNull(figure.getAnniversary())) {
      sb = replacePattern(sb.toString());
      appendAttr(sb, "~" + figure.getAnniversary() + "th Anniversary Ver.~");
    }
    if (figure.isHongKongVersion()) {
      appendAttr(sb, "~HK Version~");
    }

    if (figure.isSurplice()) {
      appendAttr(sb, "(Surplice)");
    }

    return sb.toString();
  }

  /**
   * Add some standard tags to the figures. Normally this method should be called if we want to
   * apply certain tags to a specific set of characters, as opposite to set them directly in the
   * catalog (tags very specific).
   *
   * @param figure The list of characters.
   */
  void addStandardTags(final CharacterFigure figure) {
    // make sure the tags are not null.
    if (Objects.isNull(figure.getTags())) {
      figure.setTags(new HashSet<>());
    }
    Set<String> existingTags = figure.getTags();
    // add tags based on the name ...
    existingTags.addAll(Arrays.asList(figure.getBaseName().toLowerCase().split("\\s+")));
    // add tags based on some attributes ...
    if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
      existingTags.addAll(TAG_EX);
    }
    if (figure.getSeries() == Series.SOG) {
      existingTags.addAll(TAG_SOG);
    }
    if (figure.isRevival()) {
      existingTags.addAll(TAG_REVIVAL);
    }
    if (figure.isSet()) {
      existingTags.addAll(TAG_SET);
    }
    if (figure.isBrokenCloth()) {
      existingTags.addAll(TAG_BROKEN);
    }
    if (figure.isMetalBody()) {
      existingTags.addAll(TAG_METAL);
    }
    if (figure.isOce()) {
      existingTags.addAll(TAG_OCE);
    }
    if (figure.isHongKongVersion()) {
      existingTags.addAll(TAG_HK);
    }
    if (figure.isBronzeToGold()) {
      existingTags.addAll(TAG_BRONZE_TO_GOLD);
    }
  }

  /**
   * Return a list of only re-stocks.
   *
   * @param allCharacters All the existing characters.
   * @return List of only re-stocks.
   */
  private List<CharacterFigure> findOnlyRestocks(List<CharacterFigure> allCharacters) {
    LinkedHashSet<CharacterFigure> uniqueCharacters = new LinkedHashSet<>();
    return allCharacters.stream()
        .filter(character -> !uniqueCharacters.add(character))
        .collect(Collectors.toList());
  }

  /**
   * This method is used to prepare the figure to be displayed in the response.
   *
   * @param figure The character figure to be shown.
   */
  private void calculatePriceAndDisplayableName(final CharacterFigure figure) {
    calculateReleasePricing(figure);
    calculateDisplayableName(figure);
  }

  private void calculateReleasePricing(final Figure figure) {
    Issuance jpy = figure.getIssuanceJPY();
    Issuance mxn = figure.getIssuanceMXN();

    // the price is set here.
    if (Objects.nonNull(jpy)) {
      jpy.setReleasePrice(calculateReleasePrice(jpy.getBasePrice(), jpy.getReleaseDate()));
    }
    if (Objects.nonNull(mxn)) {
      mxn.setReleasePrice(mxn.getBasePrice());
    }
  }

  private void calculateDisplayableName(final CharacterFigure figure) {
    // the displayable name is calculated here
    figure.setDisplayableName(calculateFigureDisplayableName(figure));
    // figure.setOriginalName(null);
    // figure.setBaseName(null);
  }

  private BigDecimal calculateReleasePrice(
      final BigDecimal basePrice, final LocalDate releaseDate) {
    BigDecimal releasePrice;
    if (Objects.nonNull(basePrice) && Objects.nonNull(releaseDate)) {
      LocalDate october12019 = LocalDate.of(2019, 10, 1);
      if (releaseDate.isBefore(october12019)) {
        releasePrice = basePrice.add(basePrice.multiply(new BigDecimal(".08")));
      } else {
        releasePrice = basePrice.add(basePrice.multiply(new BigDecimal(".10")));
      }
      return releasePrice;
    }
    return null;
  }

  private StringBuilder replacePattern(String name) {
    return new StringBuilder(name.replaceFirst("~", "(").replaceFirst("~", ")"));
  }

  private void appendAttr(StringBuilder sb, String attribute) {
    sb.append(" ");
    sb.append(attribute);
  }

  private Sort getSorting() {
    List<Sort.Order> orders = new ArrayList<>();
    orders.add(new Sort.Order(Sort.Direction.DESC, "futureRelease"));
    orders.add(new Sort.Order(Sort.Direction.DESC, "issuanceJPY.releaseDate"));
    return Sort.by(orders);
  }
}
