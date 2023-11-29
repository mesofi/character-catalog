/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.INVALID_IMAGE_URL;
import static com.mesofi.collection.charactercatalog.service.CharacterFigureService.INVALID_ORDER_NUMBER;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The image properties.
 * 
 * @author armandorivasarzaluz
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GalleryImage {
    private String idImage;

    @NotBlank(message = INVALID_IMAGE_URL)
    private String url;

    @EqualsAndHashCode.Exclude
    private boolean official;

    @EqualsAndHashCode.Exclude
    private boolean coverPhoto;

    @EqualsAndHashCode.Exclude
    @Positive(message = INVALID_ORDER_NUMBER)
    private int order;
}
