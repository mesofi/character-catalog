/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.entity;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * The actual entity used to interact with the repository.
 * 
 * @author armandorivasarzaluz
 *
 */
@Getter
@Setter
public class CharacterFigureEntity {
    @Id
    public String id;
}
