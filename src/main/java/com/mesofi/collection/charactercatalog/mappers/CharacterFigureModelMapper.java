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

    @Mapping(source = "issuanceJPY.basePrice", target = "basePriceJPY")
    @Mapping(source = "issuanceJPY.releasePrice", target = "releasePriceJPY")
    @Mapping(source = "issuanceJPY.firstAnnouncementDate", target = "firstAnnouncementDateJPY")
    @Mapping(source = "issuanceJPY.preorderDate", target = "preorderDateJPY")
    @Mapping(source = "issuanceJPY.preorderConfirmationDay", target = "preorderConfirmationDayJPY")
    @Mapping(source = "issuanceJPY.releaseDate", target = "releaseDateJPY")
    @Mapping(source = "issuanceJPY.releaseConfirmationDay", target = "releaseConfirmationDayJPY")
    @Mapping(source = "issuanceMXN.basePrice", target = "basePriceMXN")
    @Mapping(source = "issuanceMXN.releasePrice", target = "releasePriceMXN")
    @Mapping(source = "issuanceMXN.firstAnnouncementDate", target = "firstAnnouncementDateMXN")
    @Mapping(source = "issuanceMXN.preorderDate", target = "preorderDateMXN")
    @Mapping(source = "issuanceMXN.preorderConfirmationDay", target = "preorderConfirmationDayMXN")
    @Mapping(source = "issuanceMXN.releaseDate", target = "releaseDateMXN")
    @Mapping(source = "issuanceMXN.releaseConfirmationDay", target = "releaseConfirmationDayMXN")
    CharacterFigureView toView(CharacterFigure characterFigure);

    @Mapping(source = "basePriceJPY", target = "issuanceJPY.basePrice")
    @Mapping(source = "releasePriceJPY", target = "issuanceJPY.releasePrice")
    @Mapping(source = "firstAnnouncementDateJPY", target = "issuanceJPY.firstAnnouncementDate")
    @Mapping(source = "preorderDateJPY", target = "issuanceJPY.preorderDate")
    @Mapping(source = "preorderConfirmationDayJPY", target = "issuanceJPY.preorderConfirmationDay")
    @Mapping(source = "releaseDateJPY", target = "issuanceJPY.releaseDate")
    @Mapping(source = "releaseConfirmationDayJPY", target = "issuanceJPY.releaseConfirmationDay")
    @Mapping(source = "basePriceMXN", target = "issuanceMXN.basePrice")
    @Mapping(source = "releasePriceMXN", target = "issuanceMXN.releasePrice")
    @Mapping(source = "firstAnnouncementDateMXN", target = "issuanceMXN.firstAnnouncementDate")
    @Mapping(source = "preorderDateMXN", target = "issuanceMXN.preorderDate")
    @Mapping(source = "preorderConfirmationDayMXN", target = "issuanceMXN.preorderConfirmationDay")
    @Mapping(source = "releaseDateMXN", target = "issuanceMXN.releaseDate")
    @Mapping(source = "releaseConfirmationDayMXN", target = "issuanceMXN.releaseConfirmationDay")
    CharacterFigure toModel(CharacterFigureView characterFigureView);
}
