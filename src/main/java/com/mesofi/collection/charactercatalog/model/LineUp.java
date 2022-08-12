package com.mesofi.collection.charactercatalog.model;

public enum LineUp {
    MYTH_CLOTH("myth cloth"), MYTH_CLOTH_EX("ex"), APPENDIX("appendix"), CROWN("crown"), LEGEND("legend"), DDP(
            "DD Panoramation");

    private final String friendlyName;

    LineUp(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String toString() {
        return this.friendlyName;
    }
}
