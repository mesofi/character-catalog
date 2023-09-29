/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

/**
 * Just loads the Application Context.
 * 
 * @author armandorivasarzaluz
 *
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class CharacterCatalogApplicationIT {

    @Test
    void contextLoads() {
        log.debug("Loaded correctly!!");
    }
}
