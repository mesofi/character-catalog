/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import org.mapstruct.Mapper;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;

/**
 * The actual Character Figure model mapper.
 *
 * @author armandorivasarzaluz
 */
@Mapper(componentModel = "spring")
public interface CharacterFigureModelMapper {
    CharacterFigureEntity toEntity(CharacterFigure characterFigure);
}
