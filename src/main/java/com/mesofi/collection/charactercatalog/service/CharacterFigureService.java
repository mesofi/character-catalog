/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author armandorivasarzaluz
 */
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
    public static final String HOST_IMAGE_PREFIX = "https://imagizer.imageshack.com/v2/" + HOST_IMAGE_SIZE + "q70/";
    public static final String NO_IMAGE_URL = HOST_IMAGE_PREFIX + "923/3hbcya.png";

    public static final String TAG_EX = "ex";
    public static final String TAG_SOG = "soul,gold,god";
    public static final String TAG_REVIVAL = "revival";
    public static final String TAG_SET = "set";
    public static final String TAG_BROKEN = "broken";
    public static final String TAG_METAL = "metal";
    public static final String TAG_OCE = "oce,original,color";
    public static final String TAG_HK = "asia";
    public static final String TAG_BRONZE_TO_GOLD = "golden";

    /**
     * Loads all the characters.
     * 
     * @param file The reference to the file with all the records.
     * @return The total of records loaded.
     */
    public long loadAllCharacters(MultipartFile file) {
        return 0;
    }

}
