/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;

public interface CharacterRepository extends MongoRepository<CharacterFigure, String> {

}
