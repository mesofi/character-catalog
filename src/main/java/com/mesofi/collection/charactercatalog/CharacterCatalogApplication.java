/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mesofi.collection.charactercatalog.model.CharacterFigure;
import com.mesofi.collection.charactercatalog.repository.CharacterRepository;

@SpringBootApplication
//@EnableConfigurationProperties(CharacterConfig.class)
public class CharacterCatalogApplication implements CommandLineRunner {

    @Autowired
    private CharacterRepository characterRepository;

    public static void main(String[] args) {
        SpringApplication.run(CharacterCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("dssssd");
        CharacterFigure character = new CharacterFigure();
        character.setAnniversary(10);
        character.setBaseName("Seiya");
        character.setFirstAnnouncementDate(new Date());
        character.setReleaseDate(new Date());

        characterRepository.save(character);
        System.out.println("Saved");

        System.out.println(characterRepository.findAll().size());
    }
}
