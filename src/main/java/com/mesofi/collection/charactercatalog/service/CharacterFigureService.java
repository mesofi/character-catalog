/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mesofi.collection.charactercatalog.controllers.CarMapper;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.CharacterFigureResponse;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.Figure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockFigure;
import com.mesofi.collection.charactercatalog.model.Series;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CharacterFigureService {

    private final CharacterRepository characterRepository;
    private final CarMapper carMapper;

    public void loadAllRecords(final MultipartFile file) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new IllegalArgumentException("");
        }
        // removes all the records first.
        characterRepository.deleteAll();

        List<CharacterFigure> effectiveCharacters = new ArrayList<>();
        // @formatter:off
        List<CharacterFigure> allCharacters = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .skip(1) // we skip the header.
                .map(this::fromLineToCharacterFigure)
                .toList();
        // @formatter:on
        for (CharacterFigure curr : allCharacters) {
            if (!effectiveCharacters.contains(curr)) {
                effectiveCharacters.add(curr);
            } else {
                // add this character as re-stock
                CharacterFigure other = effectiveCharacters.get(effectiveCharacters.indexOf(curr));
                copyRestock(curr, other);
            }
        }
        // saves the records in the DB.
        characterRepository.saveAll(effectiveCharacters);
    }

    private void copyRestock(CharacterFigure restock, CharacterFigure source) {
        List<RestockFigure> restockList = source.getRestocks();
        if (Objects.isNull(restockList)) {
            source.setRestocks(new ArrayList<>());
        }
        // the source is added itself as re-stock.
        List<RestockFigure> restocks = source.getRestocks();
        RestockFigure restockFigure = new RestockFigure();
        copyCommonInfo(source, restockFigure);
        restocks.add(restockFigure);

        // now the base info is updated.
        copyCommonInfo(restock, source);
        source.setOriginalName(restock.getOriginalName());
        source.setBaseName(restock.getBaseName());
        source.setLineUp(restock.getLineUp());
        source.setSeries(restock.getSeries());
        source.setGroup(restock.getGroup());
        source.setMetalBody(restock.isMetalBody());
        source.setOce(restock.isOce());
        source.setRevival(restock.isRevival());
        source.setPlainCloth(restock.isPlainCloth());
        source.setBroken(restock.isBroken());
        source.setGolden(restock.isGolden());
        source.setGold(restock.isGold());
        source.setHk(restock.isHk());
        source.setManga(restock.isManga());
        source.setSurplice(restock.isSurplice());
        source.setSet(restock.isSet());
        source.setAnniversary(restock.getAnniversary());
    }

    private void copyCommonInfo(Figure from, Figure target) {
        target.setBasePrice(from.getBasePrice());
        target.setOfficialPrice(from.getOfficialPrice());
        target.setFirstAnnouncementDate(from.getFirstAnnouncementDate());
        target.setPreOrderConfirmedDayDate(from.getPreOrderConfirmedDayDate());
        target.setPreOrderDate(from.getPreOrderDate());
        target.setReleaseConfirmedDayDate(from.getReleaseConfirmedDayDate());
        target.setReleaseDate(from.getReleaseDate());
        target.setDistribution(from.getDistribution());
        target.setUrl(from.getUrl());
        target.setRemarks(from.getRemarks());
    }

    private CharacterFigure fromLineToCharacterFigure(final String line) {
        String[] columns = line.split("\t");

        CharacterFigure characterFigure = new CharacterFigure();

        characterFigure.setOriginalName(columns[0]);
        characterFigure.setBaseName(columns[1]);
        characterFigure.setBasePrice(getAmount(columns[2]));
        characterFigure.setFirstAnnouncementDate(getDate(columns[4], true));

        String preorderDate = columns[5];
        Boolean preorderConfirmedDay = isConfirmedDay(preorderDate);
        characterFigure.setPreOrderConfirmedDayDate(preorderConfirmedDay);
        characterFigure.setPreOrderDate(getDate(preorderDate, preorderConfirmedDay));

        String releaseDate = columns[6];
        Boolean releaseConfirmedDay = isConfirmedDay(releaseDate);
        characterFigure.setReleaseConfirmedDayDate(releaseConfirmedDay);
        characterFigure.setReleaseDate(getDate(releaseDate, releaseConfirmedDay));

        characterFigure.setDistribution(getDistribution(columns[7]));

        characterFigure.setUrl(columns[8]);
        characterFigure.setLineUp(getLineUp(columns[9]));
        characterFigure.setSeries(getSeries(columns[10]));
        characterFigure.setGroup(getGroup(columns[11]));
        characterFigure.setMetalBody(getFlag(columns[12]));
        characterFigure.setOce(getFlag(columns[13]));
        characterFigure.setRevival(getFlag(columns[14]));
        characterFigure.setPlainCloth(getFlag(columns[16]));
        characterFigure.setBroken(getFlag(columns[17]));
        characterFigure.setGolden(getFlag(columns[18]));
        characterFigure.setGold(getFlag(columns[19]));
        characterFigure.setHk(getFlag(columns[20]));
        characterFigure.setManga(getFlag(columns[21]));
        characterFigure.setSurplice(getFlag(columns[22]));
        characterFigure.setSet(getFlag(columns[23]));

        if (columns.length == 25) {
            characterFigure.setAnniversary(getNumber(columns[24]));
        }
        if (columns.length == 26) {
            characterFigure.setAnniversary(getNumber(columns[24]));
            characterFigure.setRemarks(columns[25]);
        }

        return characterFigure;
    }

    private Boolean isConfirmedDay(String value) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '/') {
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return count != 1;
    }

    private Date getDate(String value, Boolean dayMonthYear) {
        if (StringUtils.hasText(value) && Objects.nonNull(dayMonthYear)) {

            DateFormat sf = dayMonthYear ? new SimpleDateFormat("MM/dd/yyyy") : new SimpleDateFormat("MM/yyyy");
            try {
                return sf.parse(value);
            } catch (ParseException e) {

            }

        }
        return null;
    }

    private Integer getNumber(String value) {
        if (StringUtils.hasText(value)) {

            return Integer.parseInt(value);

        }
        return null;
    }

    private BigDecimal getAmount(String value) {
        if (StringUtils.hasText(value)) {
            return new BigDecimal(value.substring(1).replaceAll(",", ""));
        }
        return null;
    }

    private boolean getFlag(String value) {
        return "TRUE".equals(value);
    }

    private Group getGroup(String value) {
        for (int i = 0; i < Group.values().length; i++) {
            Group d = Group.values()[i];
            if (d.getFriendlyName().equals(value)) {
                return d;
            }
        }
        return null;
    }

    private LineUp getLineUp(String value) {
        for (int i = 0; i < LineUp.values().length; i++) {
            LineUp d = LineUp.values()[i];
            if (d.getFriendlyName().equals(value)) {
                return d;
            }
        }
        return null;
    }

    private Series getSeries(String value) {
        for (int i = 0; i < Series.values().length; i++) {
            Series d = Series.values()[i];
            if (d.getFriendlyName().equals(value)) {
                return d;
            }
        }
        return null;
    }

    private Distribution getDistribution(String value) {
        for (int i = 0; i < Distribution.values().length; i++) {
            Distribution d = Distribution.values()[i];
            if (d.getFriendlyName().equals(value)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Creates a new character.
     * 
     * @param characterFigure The new character to be created.
     * @return The character created.
     */
    public CharacterFigure createNewCharacter(final CharacterFigure characterFigure) {
        log.debug("Creating a new character ...");
        if (Objects.isNull(characterFigure)) {
            throw new IllegalArgumentException("Unable to create a new character, provide a valid reference");
        }
        if (!StringUtils.hasText(characterFigure.getBaseName())) {
            throw new IllegalArgumentException("Provide a base name for the character to be created");
        }

        if (Objects.isNull(characterFigure.getLineUp())) {
            throw new IllegalArgumentException("Provide a valid LineUp for the character");
        }

        // saves the new record in the BD.
        return characterRepository.save(characterFigure);
    }

    public void calculateFigureName(final CharacterFigure figure) {
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

        if (figure.isGolden()) {
            if (figure.getLineUp() == LineUp.MYTH_CLOTH) {
                if (figure.getGroup() == Group.V1) {
                    sb = replacePatter(sb.toString());
                    appendAttr(sb, "~Limited Gold~");
                }
                if (figure.getGroup() == Group.V2) {
                    appendAttr(sb, "~Power of Gold~");
                }
            }
            if (figure.getLineUp() == LineUp.MYTH_CLOTH_EX) {
                if (figure.getGroup() == Group.V2 || figure.getGroup() == Group.V3) {
                    sb = replacePatter(sb.toString());
                    appendAttr(sb, "~Golden Limited Edition~");
                }
            }
        }

        if (figure.isManga()) {
            appendAttr(sb, "~Comic Version~");
        }
        if (figure.isOce()) {
            sb = replacePatter(sb.toString());
            appendAttr(sb, "~Original Color Edition~");
        }
        if (Objects.nonNull(figure.getAnniversary())) {
            sb = replacePatter(sb.toString());
            appendAttr(sb, "~" + figure.getAnniversary() + "th Anniversary Ver.~");
        }
        if (figure.isHk()) {
            appendAttr(sb, "~HK Version~");
        }

        if (figure.isSurplice()) {
            appendAttr(sb, "(Surplice)");
        }

        // sets the displayed name ...
        figure.setDisplayedName(sb.toString());
    }

    private StringBuilder replacePatter(String name) {
        return new StringBuilder(name.replaceFirst("~", "(").replaceFirst("~", ")"));
    }

    private void appendAttr(StringBuilder sb, String attribute) {
        sb.append(" ");
        sb.append(attribute);
    }

    public List<CharacterFigureResponse> getAll() {
        return characterRepository.findAll().stream().map(this::ddss).collect(Collectors.toList());
    }

    private CharacterFigureResponse ddss(CharacterFigure d) {

        return carMapper.toDto(d);

    }

    private LocalDate aa(Date firstAnnouncementDate) {
        if (Objects.nonNull(firstAnnouncementDate)) {
            return firstAnnouncementDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}
