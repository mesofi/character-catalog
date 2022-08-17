package com.mesofi.collection.charactercatalog.exceptions;

public class NoSuchCharacterFound extends RuntimeException {

    private static final long serialVersionUID = 8136891257177729072L;

    public NoSuchCharacterFound(String message) {
        super(message);
    }
}
