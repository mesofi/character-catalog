/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas Arzaluz, Nov 27, 2023.
 */
package com.mesofi.collection.charactercatalog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for {@link GalleryImage}
 * 
 * @author armandorivasarzaluz
 */
@ExtendWith(MockitoExtension.class)
public class GalleryImageTest {

    private GalleryImage galleryImage;

    @Mock
    private GalleryImage galleryImageMock;

    @BeforeEach
    public void beforeEach() {
        galleryImage = new GalleryImage();
    }

    @ParameterizedTest
    @MethodSource("provideBooleanValues")
    public void should_verify_getter_setter_properties(boolean official, boolean cover) {
        galleryImage.setIdImage("3209320mj0uyg387");
        galleryImage.setUrl("https://imagizer.imageshack.com/v2/1024x768q70/924/hbuF2m.jpg");
        galleryImage.setOfficial(official);
        galleryImage.setCoverPhoto(cover);
        galleryImage.setOrder(2);

        assertEquals("3209320mj0uyg387", galleryImage.getIdImage());
        assertEquals("https://imagizer.imageshack.com/v2/1024x768q70/924/hbuF2m.jpg", galleryImage.getUrl());
        assertEquals(official, galleryImage.isOfficial());
        assertEquals(cover, galleryImage.isCoverPhoto());
        assertEquals(2, galleryImage.getOrder());
    }

    @Test
    public void should_verify_equality_1() {
        GalleryImage otherGalleryImage = galleryImage;
        assertTrue(galleryImage.equals(otherGalleryImage));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void should_verify_equality_2() {
        assertFalse(galleryImage.equals(""));
    }

    @Test
    public void should_verify_equality_3() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(false);
        assertFalse(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_equality_4() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(true);
        when(galleryImageMock.getIdImage()).thenReturn("idImage1");
        
        assertFalse(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_equality_5() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(true);
        
        galleryImage.setIdImage("idImage2");
        when(galleryImageMock.getIdImage()).thenReturn("idImage1");

        assertFalse(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_equality_6() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(true);
        
        galleryImage.setIdImage("idImage1");
        when(galleryImageMock.getIdImage()).thenReturn("idImage1");

        when(galleryImageMock.getUrl()).thenReturn("idUrl1");

        assertFalse(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_equality_7() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(true);
        
        galleryImage.setIdImage("idImage1");
        when(galleryImageMock.getIdImage()).thenReturn("idImage1");

        galleryImage.setUrl("idUrl2");
        when(galleryImageMock.getUrl()).thenReturn("idUrl1");

        assertFalse(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_equality_8() {
        when(galleryImageMock.canEqual(galleryImage)).thenReturn(true);
        
        galleryImage.setIdImage("idImage1");
        when(galleryImageMock.getIdImage()).thenReturn("idImage1");

        galleryImage.setUrl("idUrl1");
        when(galleryImageMock.getUrl()).thenReturn("idUrl1");

        assertTrue(galleryImage.equals(galleryImageMock));
    }

    @Test
    public void should_verify_same_type() {
        GalleryImage other = new GalleryImage(null, null, false, false, 0);
        assertTrue(galleryImage.canEqual(other));
        assertFalse(galleryImage.canEqual("Other"));
    }

    @Test
    public void should_get_hash_code() {
        assertEquals(6061, galleryImage.hashCode());

        galleryImage.setIdImage("1234");
        assertEquals(89060602, galleryImage.hashCode());

        galleryImage.setUrl("https://imagizer.imageshack.com/v2/1024x768q70/924/hbuF2m.jpg");
        assertEquals(2016231658, galleryImage.hashCode());
    }

    private static Stream<Arguments> provideBooleanValues() {
        // @formatter:off
        return Stream.of(
                Arguments.of(true, true),
                Arguments.of(false, false)
        );
        // @formatter:on
    }
}
