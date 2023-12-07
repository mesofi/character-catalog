/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Mock class.
 * 
 * @author armandorivasarzaluz
 */
public class MockMultipartFile implements MultipartFile {

    private boolean throwError;
    private byte[] content;

    public MockMultipartFile(boolean throwError, @Nullable byte[] content) {
        this.throwError = throwError;
        this.content = content;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (throwError) {
            throw new IOException("Some error");
        } else {
            return new ByteArrayInputStream(content);
        }
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }
}
