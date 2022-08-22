package com.mesofi.collection.charactercatalog.exceptions;

public class NoSuchCharacterFoundException extends RuntimeException {

    private static final long serialVersionUID = 8136891257177729072L;

    public NoSuchCharacterFoundException(String message) {
        super(message);
    }
}
