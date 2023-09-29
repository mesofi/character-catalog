/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Just loads the Application Context.
 * 
 * @author armandorivasarzaluz
 *
 */
@SpringBootApplication
public class CharacterCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterCatalogApplication.class, args);
    }
}
