/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Jan 16, 2024.
 */
package com.mesofi.collection.charactercatalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import com.mesofi.collection.charactercatalog.entity.CharacterFigureEntity;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.Issuance;
import com.mesofi.collection.charactercatalog.model.Series;

import lombok.extern.slf4j.Slf4j;

/**
 * Test the connectivity and integration with the persistence layer.
 * 
 * @author armandorivasarzaluz
 */
@Slf4j
@DataMongoTest
@ActiveProfiles("itest")
public class CharacterFigureRepositoryIT {

    @Autowired
    private CharacterFigureRepository characterFigureRepository;

    @BeforeEach
    void setup() {
        characterFigureRepository.deleteAll();
    }

    @Test
    public void should_perform_regular_save_find_update_and_delete_operations() {
        log.debug("Performing DB operations ...");

        // make sure there's no entities saved yet.
        assertTrue(characterFigureRepository.findAll().isEmpty());
        log.debug("No entities yet");

        // creates the first one.
        CharacterFigureEntity newEntity = new CharacterFigureEntity();
        newEntity.setBaseName("Seiya");
        Issuance issuance = new Issuance();
        issuance.setBasePrice(new BigDecimal("12.000"));
        issuance.setPreorderDate(LocalDate.of(2023, 2, 3));
        newEntity.setIssuanceJPY(issuance);
        newEntity.setGroup(Group.V1);
        newEntity.setSeries(Series.SAINT_SEIYA);

        CharacterFigureEntity saved = characterFigureRepository.save(newEntity);
        log.debug("Character saved correctly with id: {}", saved.getId());

        // finds the entity just saved
        assertEquals(1, characterFigureRepository.findAll().size());

        // gets the existing character ...
        CharacterFigureEntity found = characterFigureRepository.findById(saved.getId()).orElseThrow();
        assertNotNull(found);
        assertEquals(24, found.getId().length());
        assertEquals("Seiya", found.getBaseName());
        assertNotNull(found.getIssuanceJPY());
        assertNull(found.getIssuanceMXN());
        assertEquals(new BigDecimal("12.000"), found.getIssuanceJPY().getBasePrice());
        assertEquals(LocalDate.of(2023, 2, 3), found.getIssuanceJPY().getPreorderDate());
        assertEquals(Group.V1, found.getGroup());
        assertEquals(Series.SAINT_SEIYA, found.getSeries());

        // Update the existing one
        found.setSeries(Series.LOST_CANVAS);
        characterFigureRepository.save(found);

        CharacterFigureEntity updated = characterFigureRepository.findById(saved.getId()).orElseThrow();
        assertEquals(Series.LOST_CANVAS, updated.getSeries());

        // deletes the entity
        characterFigureRepository.deleteById(saved.getId());
        assertFalse(characterFigureRepository.findById(saved.getId()).isPresent());
    }
}
