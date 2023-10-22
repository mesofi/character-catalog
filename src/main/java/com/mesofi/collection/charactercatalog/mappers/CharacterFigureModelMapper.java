/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.views.CharacterFigureView;

/**
 * The actual Character Figure model mapper.
 *
 * @author armandorivasarzaluz
 */
@Mapper(componentModel = "spring")
public interface CharacterFigureModelMapper {

    @Mapping(source = "metalBody", target = "metal")
    @Mapping(source = "bronzeToGold", target = "golden")
    @Mapping(source = "hongKongVersion", target = "hk")
    CharacterFigureEntity toEntity(CharacterFigure characterFigure);

    @Mapping(source = "metal", target = "metalBody")
    @Mapping(source = "golden", target = "bronzeToGold")
    @Mapping(source = "hk", target = "hongKongVersion")
    CharacterFigure toModel(CharacterFigureEntity characterFigureEntity);

    @Mapping(source = "issuanceJPY.releasePrice", target = "releasePrice")
    @Mapping(source = "issuanceJPY.preorderDate", target = "preorderDate")
    @Mapping(source = "issuanceJPY.preorderConfirmationDay", target = "preorderConfirmationDay")
    @Mapping(source = "issuanceJPY.releaseDate", target = "releaseDate")
    @Mapping(source = "issuanceJPY.releaseConfirmationDay", target = "releaseConfirmationDay")
    CharacterFigureView toView(CharacterFigure characterFigure);

    CharacterFigure toModel(CharacterFigureView characterFigureView);
}
