/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Sep 19, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/**
 * Test for {@link FileUtils}
 * 
 * @author armandorivasarzaluz
 *
 */
public class FileUtilsTest {

    @Test
    public void should_fail_when_classpath_is_missing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> FileUtils.getPathFromClassPath(null));
        assertEquals("Provide a valid reference from classpath", exception.getMessage());
    }

    @Test
    public void should_fail_when_classpath_file_does_not_exist() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> FileUtils.getPathFromClassPath("unknown"));
        assertEquals("Unable to load file from: unknown", exception.getMessage());
    }

    @Test
    public void should_get_valid_classpath_location() {
        Path path = FileUtils.getPathFromClassPath("characters/MythCloth Catalog - CatalogMyth-min.tsv");
        assertNotNull(path);
        assertTrue(path.toString().contains("characters/MythCloth Catalog - CatalogMyth-min.tsv"));
    }
}
