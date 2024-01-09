/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 30, 2023.
 */
package com.mesofi.collection.charactercatalog.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

/**
 * Test for {@link CharacterFigureModelMapperImpl}
 * 
 * @author armandorivasarzaluz
 */
public class CharacterFigureModelMapperImplTest {
    private CharacterFigureModelMapper characterFigureModelMapper;

    @BeforeEach
    public void beforeEach() {
        characterFigureModelMapper = new CharacterFigureModelMapperImpl();
    }

    //@Test
    public void should_return_null_when_entity_is_null() {
        assertNull(characterFigureModelMapper.toEntity(null));
    }

    //@Test
    public void should_perform_basic_mapping_from_to_entity() {
        Issuance issuanceJPY = new Issuance();
        Issuance issuanceMXN = new Issuance();
        GalleryImage galleryImage = new GalleryImage();

        CharacterFigure characterFigure = new CharacterFigure();
        characterFigure.setMetalBody(true);
        characterFigure.setBronzeToGold(true);
        characterFigure.setHongKongVersion(true);
        characterFigure.setIssuanceJPY(issuanceJPY);
        characterFigure.setIssuanceMXN(issuanceMXN);
        characterFigure.setFutureRelease(true);
        characterFigure.setUrl("www.image.com");
        characterFigure.setDistribution(Distribution.OTHER);
        characterFigure.setRemarks("MyRemarks");
        characterFigure.setId("1");
        characterFigure.setOriginalName("Seiya");
        characterFigure.setBaseName("Seiya");
        characterFigure.setLineUp(LineUp.APPENDIX);
        characterFigure.setSeries(Series.LEGEND);
        characterFigure.setGroup(Group.BLACK);
        characterFigure.setOce(true);
        characterFigure.setRevival(true);
        characterFigure.setPlainCloth(true);
        characterFigure.setBrokenCloth(true);
        characterFigure.setGold(true);
        characterFigure.setManga(true);
        characterFigure.setSurplice(true);
        characterFigure.setSet(true);
        characterFigure.setAnniversary(20);
        characterFigure.setTags(Set.of("gold"));
        characterFigure.setImages(List.of(galleryImage));

        CharacterFigureEntity entity = characterFigureModelMapper.toEntity(characterFigure);
        assertTrue(entity.isMetal());
        assertTrue(entity.isGolden());
        assertTrue(entity.isHk());
        assertEquals(issuanceJPY, entity.getIssuanceJPY());
        assertEquals(issuanceMXN, entity.getIssuanceMXN());
        assertTrue(entity.isFutureRelease());
        assertEquals("www.image.com", entity.getUrl());
        assertEquals(Distribution.OTHER, entity.getDistribution());
        assertEquals("MyRemarks", entity.getRemarks());
        assertEquals("1", entity.getId());
        assertEquals("Seiya", entity.getOriginalName());
        assertEquals("Seiya", entity.getBaseName());
        assertEquals(LineUp.APPENDIX, entity.getLineUp());
        assertEquals(Series.LEGEND, entity.getSeries());
        assertEquals(Group.BLACK, entity.getGroup());
        assertTrue(entity.isOce());
        assertTrue(entity.isRevival());
        assertTrue(entity.isPlainCloth());
        assertTrue(entity.isBrokenCloth());
        assertTrue(entity.isGold());
        assertTrue(entity.isManga());
        assertTrue(entity.isSurplice());
        assertTrue(entity.isSet());
        assertEquals(20, entity.getAnniversary());
        assertEquals(Set.of("gold"), entity.getTags());
        assertEquals(List.of(galleryImage), entity.getImages());
    }

    //@Test
    public void should_return_null_when_model_is_null() {
        assertNull(characterFigureModelMapper.toModel(null));
    }

    //@Test
    public void should_perform_basic_mapping_from_to_model() {
        Issuance issuanceJPY = new Issuance();
        Issuance issuanceMXN = new Issuance();
        GalleryImage galleryImage = new GalleryImage();

        CharacterFigureEntity characterFigureEntity = new CharacterFigureEntity();
        characterFigureEntity.setMetal(true);
        characterFigureEntity.setGolden(true);
        characterFigureEntity.setHk(true);
        characterFigureEntity.setIssuanceJPY(issuanceJPY);
        characterFigureEntity.setIssuanceMXN(issuanceMXN);
        characterFigureEntity.setFutureRelease(true);
        characterFigureEntity.setUrl("www.image.com");
        characterFigureEntity.setDistribution(Distribution.OTHER);
        characterFigureEntity.setRemarks("MyRemarks");
        characterFigureEntity.setId("1");
        characterFigureEntity.setOriginalName("Seiya");
        characterFigureEntity.setBaseName("Seiya");
        characterFigureEntity.setLineUp(LineUp.APPENDIX);
        characterFigureEntity.setSeries(Series.LEGEND);
        characterFigureEntity.setGroup(Group.BLACK);
        characterFigureEntity.setOce(true);
        characterFigureEntity.setRevival(true);
        characterFigureEntity.setPlainCloth(true);
        characterFigureEntity.setBrokenCloth(true);
        characterFigureEntity.setGold(true);
        characterFigureEntity.setManga(true);
        characterFigureEntity.setSurplice(true);
        characterFigureEntity.setSet(true);
        characterFigureEntity.setAnniversary(20);
        characterFigureEntity.setTags(Set.of("gold"));
        characterFigureEntity.setImages(List.of(galleryImage));

        CharacterFigure model = characterFigureModelMapper.toModel(characterFigureEntity);
        assertTrue(model.isMetalBody());
        assertTrue(model.isBronzeToGold());
        assertTrue(model.isHongKongVersion());
        assertEquals(issuanceJPY, model.getIssuanceJPY());
        assertEquals(issuanceMXN, model.getIssuanceMXN());
        assertTrue(model.isFutureRelease());
        assertEquals("www.image.com", model.getUrl());
        assertEquals(Distribution.OTHER, model.getDistribution());
        assertEquals("MyRemarks", model.getRemarks());
        assertEquals("1", model.getId());
        assertEquals("Seiya", model.getOriginalName());
        assertEquals("Seiya", model.getBaseName());
        assertEquals(LineUp.APPENDIX, model.getLineUp());
        assertEquals(Series.LEGEND, model.getSeries());
        assertEquals(Group.BLACK, model.getGroup());
        assertTrue(model.isOce());
        assertTrue(model.isRevival());
        assertTrue(model.isPlainCloth());
        assertTrue(model.isBrokenCloth());
        assertTrue(model.isGold());
        assertTrue(model.isManga());
        assertTrue(model.isSurplice());
        assertTrue(model.isSet());
        assertEquals(20, model.getAnniversary());
        assertEquals(Set.of("gold"), model.getTags());
        assertEquals(List.of(galleryImage), model.getImages());
    }
}
