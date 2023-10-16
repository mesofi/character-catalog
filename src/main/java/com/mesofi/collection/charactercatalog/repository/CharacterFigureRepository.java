/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import org.springframework.stereotype.Repository;

/**
 * The repository.
 * 
 * @author armandorivasarzaluz
 *
 */
@Primary // we make it primary because the Integration Tests uses a custom implementation.
@Repository
public interface CharacterFigureRepository extends MongoRepository<CharacterFigureEntity, String> {
}
