package com.mesofi.collection.charactercatalog.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CharacterFigure extends Figure {
    private String baseName; // Name of the character
    private String displayedName; // Names to be displayed based on attributes of the figure.

    private LineUp lineUp; // MythCloth ... MythCloth EX etc.
    private Group group = Group.OTHER; // This figure belongs to a certain group

    private boolean metalBody;
    private boolean oce;
    private boolean revival;
    private boolean plainCloth;
    private boolean broken;
    private boolean golden;
    private boolean gold;
    private boolean hk;
    private boolean manga;
    private boolean surplice;
    private boolean set;
    private Integer anniversary;
    private List<RestockFigure> restocks;
}
