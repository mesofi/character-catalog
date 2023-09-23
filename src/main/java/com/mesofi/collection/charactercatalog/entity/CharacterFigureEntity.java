/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document("CharacterFigure")
public class CharacterFigureEntity {
    @Id
    private String id;
    private String originalName;
    private String baseName;
    private BigDecimal basePrice;
    private Date firstAnnouncementDate;
    private Date preorderDate;
    private Date releaseDate;
}
