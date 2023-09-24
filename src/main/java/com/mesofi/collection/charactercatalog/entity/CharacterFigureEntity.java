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

import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

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
    private String url;
    private Distribution distribution;
    private LineUp lineUp;
    private Series series;
    private Group group;
    private boolean metal;
    private boolean oce;
    private boolean revival;
    private boolean plainCloth;
    private boolean brokenCloth;
    private boolean golden;
    private boolean gold;
    private boolean hk;
    private boolean manga;
    private boolean surplice;
    private boolean set;
    private Integer anniversary;
    private String remarks;
}
