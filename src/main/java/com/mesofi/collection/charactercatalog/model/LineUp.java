package com.mesofi.collection.charactercatalog.model;

public enum LineUp {
    // @formatting:off
    MYTH_CLOTH("Myth Cloth"), MYTH_CLOTH_EX("Myth Cloth EX"), APPENDIX("Appendix"), CROWN("Saint Cloth Crown"),
    LEGEND("Saint Cloth Legend"), DDP("DD Panoramation"), FIGUARTS("Figuarts");
    // @formatting:on

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
