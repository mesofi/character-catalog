/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Objects;

import org.springframework.util.ResourceUtils;

/**
 * File utilities.
 * 
 * @author armandorivasarzaluz
 */
public class FileUtils {

    private FileUtils() {

    }

    /**
     * Gets a reference of the file located in a classPath.
     * 
     * @param classpath The classPath location.
     * @return The reference of the file.
     */
    public static Path getPathFromClassPath(final String classpath) {
        if (Objects.isNull(classpath)) {
            throw new IllegalArgumentException("Provide a valid reference from classpath");
        }
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + classpath);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to load file from: " + classpath, e);
        }
        return file.toPath();
    }
}
