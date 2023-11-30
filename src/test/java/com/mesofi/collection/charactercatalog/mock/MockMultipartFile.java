/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 29, 2023.
 */
package com.mesofi.collection.charactercatalog.mock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * Mock class.
 * 
 * @author armandorivasarzaluz
 */
public class MockMultipartFile implements MultipartFile {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOriginalFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new IOException("Some error");
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        // TODO Auto-generated method stub

    }

}
