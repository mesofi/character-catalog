/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.DEFAULT_JPG_EXT;
import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.DEFAULT_PNG_EXT;
import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.HOST_IMAGE_PREFIX;
import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.NO_IMAGE_URL;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.isDayMonthYear;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toBoolean;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toDate;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toInteger;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toListValue;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toPrice;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toSetValue;
import static com.mesofi.collection.charactercatalog.utils.CommonUtils.toStringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

/**
 * The actual Character Figure file mapper.
 *
 * @author armandorivasarzaluz
 */
@Component
public class CharacterFigureFileMapper {

    /**
     * Converts a plain line to a character figure object.
     *
     * @param line The line to be parsed and converted.
     * @return The character figure.
     */
    public CharacterFigure fromLineToCharacterFigure(final String line) {
        if (!StringUtils.hasText(line)) {
            return null;
        }
        String[] columns = line.split("\t");

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setOriginalName(columns[0]);
        characterFigure.setBaseName(columns[1]);
        characterFigure.setIssuanceJPY(createIssuance(columns[2], columns[4], columns[5], columns[6]));
        characterFigure.setIssuanceMXN(createIssuance(columns[7], null, columns[8], columns[9]));
        characterFigure.setFutureRelease(!StringUtils.hasText(columns[6]));
        characterFigure.setUrl(toStringValue(columns[10]));
        characterFigure.setDistribution(toEnum(columns[11], Distribution.class));
        characterFigure.setLineUp(toEnum(columns[12], LineUp.class));
        characterFigure.setSeries(toEnum(columns[13], Series.class));
        characterFigure.setGroup(toEnum(columns[14], Group.class));
        characterFigure.setMetalBody(toBoolean(columns[15]));
        characterFigure.setOce(toBoolean(columns[16]));
        characterFigure.setRevival(toBoolean(columns[17]));
        characterFigure.setPlainCloth(toBoolean(columns[19]));
        characterFigure.setBrokenCloth(toBoolean(columns[20]));
        characterFigure.setBronzeToGold(toBoolean(columns[21]));
        characterFigure.setGold(toBoolean(columns[22]));
        characterFigure.setHongKongVersion(toBoolean(columns[23]));
        characterFigure.setManga(toBoolean(columns[24]));
        characterFigure.setSurplice(toBoolean(columns[25]));
        characterFigure.setSet(toBoolean(columns[26]));
        if (columns.length >= 28) {
            characterFigure.setAnniversary(toInteger(columns[27]));
        }
        if (columns.length >= 29) {
            characterFigure.setRemarks(toStringValue(columns[28]));
        }
        if (columns.length >= 30) {
            characterFigure.setTags(toSetValue(columns[29]));
        }

        List<String> officialImages = null;
        List<String> otherImages = null;
        if (columns.length >= 31) {
            officialImages = toListValue(columns[30]);
        }
        if (columns.length >= 32) {
            otherImages = toListValue(columns[31]);
        }
        characterFigure.setImages(createImages(officialImages, otherImages));

        return characterFigure;
    }

    private List<GalleryImage> createImages(List<String> officialImages, List<String> otherImages) {
        if (Objects.isNull(officialImages) & Objects.isNull(otherImages)) {
            // we create a default image
            return List.of(new GalleryImage(null, createImageShackUrl(null), false, true, 1));
        }
        if (Objects.nonNull(officialImages) & Objects.isNull(otherImages)) {
            return createImagesFrom(officialImages, true);
        }
        if (Objects.isNull(officialImages)) {
            return createImagesFrom(otherImages, false);
        } else {
            // both official and other images list are non null.
            List<GalleryImage> list = createImagesFrom(officialImages, true);
            if (list.isEmpty()) {
                return createImagesFrom(otherImages, false);
            } else {
                for (int i = list.size(); i < otherImages.size() + list.size(); i++) {
                    list.add(new GalleryImage(UUID.randomUUID().toString(), createImageShackUrl(otherImages.get(i)),
                            false, false, i + 1));
                }
            }
            return list;
        }
    }

    private List<GalleryImage> createImagesFrom(List<String> source, boolean official) {
        List<GalleryImage> list = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            list.add(new GalleryImage(null, createImageShackUrl(source.get(i)), official, i == 0, i + 1));
        }
        return list;
    }

    private String createImageShackUrl(String shortUrl) {
        if (StringUtils.hasText(shortUrl)) {
            String imageUrl = HOST_IMAGE_PREFIX + shortUrl;
            if (!shortUrl.endsWith(DEFAULT_PNG_EXT)) {
                if (!shortUrl.contains(DEFAULT_JPG_EXT)) {
                    imageUrl += DEFAULT_JPG_EXT;
                }
            }
            return imageUrl;
        } else {
            return NO_IMAGE_URL;
        }
    }

    public Issuance createIssuance(Issuance issuance) {
        return Objects.nonNull(issuance)
                ? createIssuance(toPrice(issuance.getBasePrice()), toDate(issuance.getFirstAnnouncementDate()),
                        toDate(issuance.getPreorderDate(),
                                Objects.isNull(issuance.getPreorderConfirmationDay()) ? null
                                        : !issuance.getPreorderConfirmationDay()),
                        toDate(issuance.getReleaseDate(), Objects.isNull(issuance.getReleaseConfirmationDay()) ? null
                                : !issuance.getReleaseConfirmationDay()))
                : null;
    }

    private Issuance createIssuance(String price, String announcement, String preorder, String release) {
        if (StringUtils.hasText(price) || StringUtils.hasText(announcement) || StringUtils.hasText(preorder)
                || StringUtils.hasText(release)) {
            Issuance issuance = new Issuance();
            issuance.setBasePrice(toPrice(price));
            issuance.setFirstAnnouncementDate(toDate(announcement));
            issuance.setPreorderDate(toDate(preorder));
            issuance.setPreorderConfirmationDay(isDayMonthYear(preorder));
            issuance.setReleaseDate(toDate(release));
            issuance.setReleaseConfirmationDay(isDayMonthYear(release));
            return issuance;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T toEnum(String value, Class<?> clazz) {
        if (clazz.isEnum()) {
            Object[] constants = clazz.getEnumConstants();
            for (Object object : constants) {
                Enum<?> e = (Enum<?>) object;
                if (e.toString().equals(value)) {
                    return (T) e;
                }
            }
        }
        return null;
    }
}
