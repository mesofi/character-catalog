/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mesofi.collection.charactercatalog.model.Figure;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.RestockFigure;
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
public class CharacterFigureEntity extends Figure {
    @Id
    private String id;
    private String originalName;
    private String baseName;
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
    private List<RestockFigure> restocks;
    private Set<String> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CharacterFigureEntity that = (CharacterFigureEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
